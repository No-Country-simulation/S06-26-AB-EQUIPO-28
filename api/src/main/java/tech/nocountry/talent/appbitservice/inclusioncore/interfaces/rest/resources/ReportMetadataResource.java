package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs.resources.MentalHealthReportResourceDocs;

/**
 * Aggregated report metadata.
 *
 * @param totalVulnerablePopulation total vulnerable population
 * @param totalPopulation total population
 * @param averageVulnerabilityScore average vulnerability score
 * @param priorityRegionCount number of priority regions
 */
public record ReportMetadataResource(
        @Schema(description = MentalHealthReportResourceDocs.META_TOTAL_VULNERABLE)
        int totalVulnerablePopulation,

        @Schema(description = MentalHealthReportResourceDocs.META_TOTAL_POPULATION)
        int totalPopulation,

        @Schema(description = MentalHealthReportResourceDocs.META_AVG_SCORE)
        int averageVulnerabilityScore,

        @Schema(description = MentalHealthReportResourceDocs.META_PRIORITY_COUNT)
        long priorityRegionCount
) {}
