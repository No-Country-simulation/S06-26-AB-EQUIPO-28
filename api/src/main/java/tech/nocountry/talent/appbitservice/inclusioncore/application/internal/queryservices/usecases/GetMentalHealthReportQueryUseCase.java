package tech.nocountry.talent.appbitservice.inclusioncore.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.aggregates.MentalHealthReport;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.queries.GetMentalHealthReportQuery;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.ConnectivityLevel;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.RegionMetric;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.VulnerabilityScore;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.TelemetryAclPort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Atomic use case to generate a mental health report.
 *
 * <p>Calculates vulnerability metrics based on network concentration and connectivity
 * data. Answers the question: "Where are there people but lack internet?"</p>
 *
 * <p>Scoring formula (0-100):</p>
 * <ul>
 *   <li>concentrationScore (0-50): based on avg user_count per cluster</li>
 *   <li>connectivityScore (0-50): based on avg drop_pct (0-30) + avg congestion_level (0-20)</li>
 * </ul>
 *
 * <p>Priority threshold: score > 50</p>
 */
@Service
@RequiredArgsConstructor
public class GetMentalHealthReportQueryUseCase {
    private final TelemetryAclPort telemetryAclPort;

    @Transactional(readOnly = true)
    public MentalHealthReport execute(GetMentalHealthReportQuery query) {
        // Get all concentration data (already includes cluster info from antennas)
        var allMetrics = telemetryAclPort.getAllConcentration();

        // Group by cluster and calculate averages
        Map<String, List<RegionMetric>> byCluster = allMetrics.stream()
                .collect(Collectors.groupingBy(RegionMetric::regionName));

        var summaries = byCluster.entrySet().stream()
                .map(entry -> {
                    var clusterName = entry.getKey();
                    var metrics = entry.getValue();

                    // Calculate averages for this cluster
                    var avgUserCount = metrics.stream()
                            .mapToDouble(m -> m.populationCount() != null ? m.populationCount() : 0.0)
                            .average().orElse(0.0);
                    var avgDropPct = metrics.stream()
                            .mapToDouble(m -> m.averageDropRate() != null ? m.averageDropRate() : 0.0)
                            .average().orElse(0.0);
                    var avgCongestion = metrics.stream()
                            .mapToDouble(m -> m.congestionLevel() != null ? m.congestionLevel() : 0.0)
                            .average().orElse(0.0);

                    // Calculate score components
                    int concentrationScore = calculateConcentrationScore(avgUserCount);
                    int dropPctScore = calculateDropPctScore(avgDropPct);
                    int congestionScore = calculateCongestionScore(avgCongestion);
                    int connectivityScore = dropPctScore + congestionScore;

                    int totalScore = Math.min(100, Math.max(0, concentrationScore + connectivityScore));

                    var score = VulnerabilityScore.of(totalScore);
                    var connectivity = ConnectivityLevel.fromDropPercentage(avgDropPct);
                    var isPriority = totalScore > 50;

                    // vulnerablePercentage = percentage of priority clusters (calculated after)
                    return MentalHealthReport.RegionVulnerabilitySummary.create(
                            clusterName, score, connectivity, 0.0, isPriority);
                })
                .filter(s -> s.vulnerabilityScore().getValue() >= query.minVulnerabilityScore())
                .filter(s -> !query.includePriorityOnly() || s.isPriority())
                .sorted((a, b) -> Integer.compare(b.vulnerabilityScore().getValue(), a.vulnerabilityScore().getValue()))
                .toList();

        // Calculate vulnerablePercentage as percentage of priority clusters
        long priorityCount = summaries.stream().filter(MentalHealthReport.RegionVulnerabilitySummary::isPriority).count();
        double vulnerablePct = summaries.isEmpty() ? 0.0 : (double) priorityCount / summaries.size() * 100.0;

        var finalSummaries = summaries.stream()
                .map(s -> MentalHealthReport.RegionVulnerabilitySummary.create(
                        s.regionName(), s.vulnerabilityScore(), s.connectivity(), vulnerablePct, s.isPriority()))
                .toList();

        // For metadata: totalVulnerable = priority clusters count, totalPop = all clusters count
        int totalVulnerable = (int) priorityCount;
        int totalPop = summaries.size();

        return MentalHealthReport.create(query.reportPeriod(), finalSummaries, totalVulnerable, totalPop);
    }

    /**
     * Maps avg user_count per cluster to 0-50 points.
     * <ul>
     *   <li>0-100 users: 0pts</li>
     *   <li>100-500 users: 10pts</li>
     *   <li>500-1000 users: 20pts</li>
     *   <li>1000-5000 users: 35pts</li>
     *   <li>5000+ users: 50pts</li>
     * </ul>
     */
    private int calculateConcentrationScore(double avgUserCount) {
        if (avgUserCount < 100) return 0;
        if (avgUserCount < 500) return 10;
        if (avgUserCount < 1000) return 20;
        if (avgUserCount < 5000) return 35;
        return 50;
    }

    /**
     * Maps avg drop_pct to 0-30 points.
     * <ul>
     *   <li>&lt; 0.02 (2%): 0pts (good)</li>
     *   <li>0.02-0.05 (2-5%): 10pts (medium)</li>
     *   <li>0.05-0.10 (5-10%): 20pts (bad)</li>
     *   <li>&gt; 0.10 (10%): 30pts (terrible)</li>
     * </ul>
     */
    private int calculateDropPctScore(double avgDropPct) {
        if (avgDropPct < 0.02) return 0;
        if (avgDropPct <= 0.05) return 10;
        if (avgDropPct <= 0.10) return 20;
        return 30;
    }

    /**
     * Maps avg congestion_level to 0-20 points.
     * <ul>
     *   <li>&lt; 0.3 (30%): 0pts (good)</li>
     *   <li>0.3-0.6 (30-60%): 10pts (medium)</li>
     *   <li>&gt; 0.6 (60%): 20pts (bad)</li>
     * </ul>
     */
    private int calculateCongestionScore(double avgCongestion) {
        if (avgCongestion < 0.3) return 0;
        if (avgCongestion <= 0.6) return 10;
        return 20;
    }
}
