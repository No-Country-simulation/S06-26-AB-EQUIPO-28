package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.aggregates;

import lombok.Getter;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.ConnectivityLevel;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.VulnerabilityScore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root representing a mental health vulnerability report for government managers.
 *
 * <p>This report provides a comprehensive view of regions where mental health programs
 * should be prioritized, based on the following criteria:</p>
 * <ul>
 *   <li>High concentration of vulnerable population (income_level = D)</li>
 *   <li>Low network connectivity (making telemedicine impossible)</li>
 *   <li>High population density without adequate infrastructure</li>
 * </ul>
 *
 * <p>The report aggregates multiple region vulnerability summaries and provides
 * executive-level summaries for decision-making.</p>
 */
@Getter
public class MentalHealthReport {
    private final String reportId;
    private final Instant generatedAt;
    private final String reportPeriod;
    private final List<RegionVulnerabilitySummary> regionSummaries;
    private final ReportMetadata metadata;

    /**
     * Creates a new MentalHealthReport.
     *
     * @param reportId unique identifier for the report
     * @param reportPeriod the period covered by the report (e.g., "2024-Q1")
     * @param regionSummaries list of region vulnerability summaries
     * @param metadata metadata about the report generation
     */
    public MentalHealthReport(
            String reportId,
            String reportPeriod,
            List<RegionVulnerabilitySummary> regionSummaries,
            ReportMetadata metadata
    ) {
        this.reportId = Objects.requireNonNull(reportId, "report ID cannot be null");
        this.generatedAt = Instant.now();
        this.reportPeriod = Objects.requireNonNull(reportPeriod, "report period cannot be null");
        this.regionSummaries = List.copyOf(regionSummaries);
        this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
    }

    /**
     * Factory method to create a MentalHealthReport with a generated report ID and period.
     *
     * @param summaries list of region vulnerability summaries
     * @return new MentalHealthReport instance
     */
    public static MentalHealthReport create(List<RegionVulnerabilitySummary> summaries) {
        String reportId = "MHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String reportPeriod = java.time.Year.now() + "-Q" +
                ((java.time.Month.from(java.time.LocalDate.now()).getValue() - 1) / 3 + 1);

        List<RegionVulnerabilitySummary> sortedSummaries = new ArrayList<>(summaries);
        sortedSummaries.sort((a, b) -> Integer.compare(b.vulnerabilityScore().getValue(), a.vulnerabilityScore().getValue()));

        ReportMetadata metadata = ReportMetadata.create(
                0,
                0,
                calculateAverageVulnerability(sortedSummaries),
                calculatePriorityCount(sortedSummaries)
        );

        return new MentalHealthReport(reportId, reportPeriod, sortedSummaries, metadata);
    }

    /**
     * Factory method to create a MentalHealthReport with an auto-generated report ID.
     *
     * @param reportPeriod the period covered by the report (e.g., "2024-Q1")
     * @param regionSummaries list of region vulnerability summaries
     * @param totalVulnerablePopulation total vulnerable population across all regions
     * @param totalPopulation total population across all regions
     * @return new MentalHealthReport instance
     */
    public static MentalHealthReport create(
            String reportPeriod,
            List<RegionVulnerabilitySummary> regionSummaries,
            int totalVulnerablePopulation,
            int totalPopulation) {
        String reportId = "MHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        List<RegionVulnerabilitySummary> sortedSummaries = new ArrayList<>(regionSummaries);
        sortedSummaries.sort((a, b) -> Integer.compare(b.vulnerabilityScore().getValue(), a.vulnerabilityScore().getValue()));

        ReportMetadata metadata = ReportMetadata.create(
                totalVulnerablePopulation,
                totalPopulation,
                calculateAverageVulnerability(sortedSummaries),
                calculatePriorityCount(sortedSummaries)
        );

        return new MentalHealthReport(reportId, reportPeriod, sortedSummaries, metadata);
    }

    /**
     * Factory method to create a MentalHealthReport with explicit report parameters.
     *
     * @param reportId unique identifier for the report
     * @param reportPeriod the period covered by the report
     * @param regionSummaries list of region vulnerability summaries
     * @param totalVulnerablePopulation total vulnerable population across all regions
     * @param totalPopulation total population across all regions
     * @return new MentalHealthReport instance
     */
    public static MentalHealthReport create(
            String reportId,
            String reportPeriod,
            List<RegionVulnerabilitySummary> regionSummaries,
            int totalVulnerablePopulation,
            int totalPopulation) {
        List<RegionVulnerabilitySummary> sortedSummaries = new ArrayList<>(regionSummaries);
        sortedSummaries.sort((a, b) -> Integer.compare(b.vulnerabilityScore().getValue(), a.vulnerabilityScore().getValue()));

        ReportMetadata metadata = ReportMetadata.create(
                totalVulnerablePopulation,
                totalPopulation,
                calculateAverageVulnerability(sortedSummaries),
                calculatePriorityCount(sortedSummaries)
        );

        return new MentalHealthReport(reportId, reportPeriod, sortedSummaries, metadata);
    }

    private static int calculateAverageVulnerability(List<RegionVulnerabilitySummary> summaries) {
        if (summaries.isEmpty()) return 0;
        int total = summaries.stream().mapToInt(s -> s.vulnerabilityScore().getValue()).sum();
        return total / summaries.size();
    }

    private static long calculatePriorityCount(List<RegionVulnerabilitySummary> summaries) {
        return summaries.stream().filter(RegionVulnerabilitySummary::isPriority).count();
    }

    /**
     * Gets priority regions for intervention.
     *
     * @return list of regions that are priority for mental health programs
     */
    public List<RegionVulnerabilitySummary> getPriorityRegions() {
        return regionSummaries.stream()
                .filter(RegionVulnerabilitySummary::isPriority)
                .toList();
    }

    /**
     * Checks if the report has any priority regions.
     *
     * @return true if there are priority regions
     */
    public boolean hasPriorityRegions() {
        return !getPriorityRegions().isEmpty();
    }

    /**
     * Inner record representing a summary of vulnerability for a single region.
     *
     * @param regionName the geographic region name
     * @param vulnerabilityScore the calculated vulnerability score (0-100)
     * @param connectivity network connectivity level
     * @param vulnerablePercentage percentage of vulnerable population in the region
     * @param isPriority whether this region is priority for intervention
     */
    public record RegionVulnerabilitySummary(
            String regionName,
            VulnerabilityScore vulnerabilityScore,
            ConnectivityLevel connectivity,
            double vulnerablePercentage,
            boolean isPriority
    ) {
        /**
         * Factory method to create a RegionVulnerabilitySummary.
         *
         * @param regionName the geographic region name
         * @param score the vulnerability score
         * @param connectivity network connectivity level
         * @param vulnerablePercentage percentage of vulnerable population
         * @param isPriority whether this region is priority for intervention
         * @return new RegionVulnerabilitySummary
         */
        public static RegionVulnerabilitySummary create(
                String regionName,
                VulnerabilityScore score,
                ConnectivityLevel connectivity,
                double vulnerablePercentage,
                boolean isPriority
        ) {
            return new RegionVulnerabilitySummary(regionName, score, connectivity, vulnerablePercentage, isPriority);
        }
    }

    /**
     * Inner record representing metadata about the report.
     *
     * @param totalVulnerablePopulation total vulnerable population
     * @param totalPopulation total population
     * @param averageVulnerabilityScore average vulnerability score across regions
     * @param priorityRegionCount number of priority regions
     */
    public record ReportMetadata(
            int totalVulnerablePopulation,
            int totalPopulation,
            int averageVulnerabilityScore,
            long priorityRegionCount
    ) {
        /**
         * Creates ReportMetadata.
         *
         * @param totalVulnerablePopulation total vulnerable population
         * @param totalPopulation total population
         * @param averageVulnerabilityScore average vulnerability score
         * @param priorityRegionCount number of priority regions
         * @return new ReportMetadata
         */
        public static ReportMetadata create(
                int totalVulnerablePopulation,
                int totalPopulation,
                int averageVulnerabilityScore,
                long priorityRegionCount) {
            return new ReportMetadata(
                    totalVulnerablePopulation,
                    totalPopulation,
                    averageVulnerabilityScore,
                    priorityRegionCount
            );
        }

        /**
         * Creates empty metadata.
         *
         * @return empty ReportMetadata
         */
        public static ReportMetadata empty() {
            return new ReportMetadata(0, 0, 0, 0);
        }

    }
}
