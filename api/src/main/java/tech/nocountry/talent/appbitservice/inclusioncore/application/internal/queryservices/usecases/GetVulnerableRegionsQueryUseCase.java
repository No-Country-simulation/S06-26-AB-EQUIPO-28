package tech.nocountry.talent.appbitservice.inclusioncore.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.queries.GetVulnerableRegionsQuery;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.ConnectivityLevel;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.RegionMetric;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.VulnerabilityScore;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.ClusterCountAclResult;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.DemographicsAclPort;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.TelemetryAclPort;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.VulnerableRegionResource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Atomic use case to retrieve vulnerable regions.
 *
 * <p>This use case retrieves regions that meet vulnerability criteria,
 * useful for targeting interventions and resource allocation.
 * Vulnerability scores are calculated on-the-fly by crossing demographic
 * and telemetry data via ACL ports.</p>
 */
@Service
@RequiredArgsConstructor
public class GetVulnerableRegionsQueryUseCase {
    private final DemographicsAclPort demographicsAclPort;
    private final TelemetryAclPort telemetryAclPort;

    /**
     * Executes the vulnerable regions query.
     *
     * @param query the query with vulnerability parameters
     * @return list of vulnerable region resources meeting the criteria
     */
    @Transactional(readOnly = true)
    public List<VulnerableRegionResource> execute(GetVulnerableRegionsQuery query) {
        // 1. Get demographic data
        var vulnerableByCluster = demographicsAclPort.getCitizenCountByCluster("D");
        var totalByCluster = demographicsAclPort.getCitizenCountByCluster(null);

        // Build lookup map for total population
        var totalByClusterMap = totalByCluster.stream()
                .collect(Collectors.toMap(ClusterCountAclResult::clusterName, ClusterCountAclResult::citizenCount));

        // 2. For each cluster with vulnerable population, calculate score
        return vulnerableByCluster.stream()
                .map(vc -> calculateRegionResource(vc, totalByClusterMap))
                .filter(r -> r.vulnerabilityScore() >= query.minVulnerabilityScore())
                .filter(r -> !query.poorConnectivityOnly() || "LOW".equalsIgnoreCase(r.connectivityLevel()))
                .sorted(Comparator.comparingInt(VulnerableRegionResource::vulnerabilityScore).reversed())
                .limit(query.maxResults())
                .toList();
    }

    private VulnerableRegionResource calculateRegionResource(
            ClusterCountAclResult vc,
            java.util.Map<String, Long> totalByClusterMap
    ) {
        var clusterName = vc.clusterName();
        var vulnerableCount = vc.citizenCount();
        var totalCount = totalByClusterMap.getOrDefault(clusterName, 0L);

        // 3. Get telemetry data for this cluster
        var concentrationMetrics = telemetryAclPort.getConcentrationByCluster(clusterName);

        // 4. Compute aggregates
        // drop_pct is a fraction [0.0-1.0]; fall back to a conservative 0.06 (LOW)
        // when no telemetry is available for the cluster.
        var avgDropPct = concentrationMetrics.stream()
                .mapToDouble(RegionMetric::averageDropRate)
                .average().orElse(0.06);
        var avgUserCount = concentrationMetrics.stream()
                .mapToDouble(m -> m.populationCount() != null ? m.populationCount().doubleValue() : 0.0)
                .average().orElse(0.0);

        // 5. Calculate vulnerability components.
        // Weighting reflects which telemetry signals actually discriminate in the
        // data: drop_pct/congestion are near-constant, so the social ratio of
        // vulnerable citizens carries the most weight, followed by user density.
        var connectivityLevel = ConnectivityLevel.fromDropPercentage(avgDropPct);
        double vulnerableRatio = totalCount > 0 ? (double) vulnerableCount / totalCount : 0.0;
        int populationFactor = (int) Math.round(vulnerableRatio * 50);
        // Divide by 50 so the observed user_count range (~1000-4100) spreads
        // across the scale instead of saturating at 100.
        int concentrationFactor = (int) Math.round(Math.min(100, avgUserCount / 50) * 0.25);
        int networkFactor = switch (connectivityLevel.getValue().toUpperCase()) {
            case "LOW" -> 25;
            case "MEDIUM" -> 12;
            default -> 0;
        };
        int totalScore = Math.min(100, Math.max(0, populationFactor + concentrationFactor + networkFactor));
        var vulnerabilityScore = VulnerabilityScore.of(totalScore);

        // 6. Build result
        var vulnerablePct = totalCount > 0 ? (double) vulnerableCount / totalCount * 100.0 : 0.0;
        var isPriority = vulnerabilityScore.isGreaterThan(VulnerabilityScore.of(60)) && connectivityLevel.isLow();

        return new VulnerableRegionResource(
                clusterName,
                vulnerabilityScore.getValue(),
                vulnerabilityScore.getLevel(),
                vulnerableCount.intValue(),
                totalCount.intValue(),
                vulnerablePct,
                connectivityLevel.getValue(),
                Math.min(100, avgUserCount / 50),
                isPriority
        );
    }
}