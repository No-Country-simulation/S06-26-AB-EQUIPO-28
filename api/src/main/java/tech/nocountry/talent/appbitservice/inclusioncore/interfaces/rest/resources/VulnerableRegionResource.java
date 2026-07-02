package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs.resources.HealthVulnerabilityResourceDocs;

/**
 * REST resource for vulnerable regions.
 *
 * <p>This DTO represents a region with a high vulnerability index
 * to be used in API responses.</p>
 */
@Schema(description = HealthVulnerabilityResourceDocs.DESCRIPTION)
public record VulnerableRegionResource(
        @Schema(description = HealthVulnerabilityResourceDocs.REGION_NAME)
        String regionName,

        @Schema(description = HealthVulnerabilityResourceDocs.VULNERABILITY_SCORE)
        int vulnerabilityScore,

        @Schema(description = HealthVulnerabilityResourceDocs.VULNERABILITY_LEVEL)
        String vulnerabilityLevel,

        @Schema(description = HealthVulnerabilityResourceDocs.VULNERABLE_POPULATION)
        int vulnerablePopulationCount,

        @Schema(description = HealthVulnerabilityResourceDocs.TOTAL_POPULATION)
        int totalPopulation,

        @Schema(description = HealthVulnerabilityResourceDocs.VULNERABLE_PERCENTAGE)
        double vulnerablePercentage,

        @Schema(description = HealthVulnerabilityResourceDocs.CONNECTIVITY_LEVEL)
        String connectivityLevel,

        @Schema(description = HealthVulnerabilityResourceDocs.CONCENTRATION_INDEX)
        double concentrationIndex,

        @Schema(description = HealthVulnerabilityResourceDocs.PRIORITY_FOR_INTERVENTION)
        boolean isPriorityForIntervention
) {
}
