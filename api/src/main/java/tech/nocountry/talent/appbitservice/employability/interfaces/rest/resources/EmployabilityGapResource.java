package tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources.EmployabilityGapResourceDocs;

import java.util.List;

/**
 * REST resource for employability gap analysis results.
 *
 * <p>Representa la brecha de empleabilidad de un cluster geográfico: cruce entre
 * la matriz OD de movilidad (este BC), los índices demográficos (ACL demographics)
 * y la telemetría diurna (ACL telemetry). Usa {@code @Schema} con descripciones
 * reutilizables desde {@link EmployabilityGapResourceDocs}.</p>
 */
@Schema(description = EmployabilityGapResourceDocs.DESCRIPTION)
public record EmployabilityGapResource(
        @Schema(description = EmployabilityGapResourceDocs.CLUSTER)
        String cluster,

        @Schema(description = EmployabilityGapResourceDocs.MUNICIPALITIES)
        List<String> municipalities,

        @Schema(description = EmployabilityGapResourceDocs.CITIZEN_COUNT)
        long citizenCount,

        @Schema(description = EmployabilityGapResourceDocs.INCOME_D_COUNT)
        long incomeDCount,

        @Schema(description = EmployabilityGapResourceDocs.INCOME_C_COUNT)
        long incomeCCount,

        @Schema(description = EmployabilityGapResourceDocs.YOUTH_COUNT_18_24)
        long youthCount18_24,

        @Schema(description = EmployabilityGapResourceDocs.HAS_TELEMETRY_COVERAGE)
        boolean hasTelemetryCoverage,

        @Schema(description = EmployabilityGapResourceDocs.DAYTIME_AVG_USERS)
        double daytimeAvgUsers,

        @Schema(description = EmployabilityGapResourceDocs.OUTBOUND_TRIPS_TO_HUBS)
        long outboundTripsToHubs,

        @Schema(description = EmployabilityGapResourceDocs.DISTANCE_TO_NEAREST_HUB_KM)
        double distanceToNearestHubKm,

        @Schema(description = EmployabilityGapResourceDocs.MOBILITY_INTENSITY)
        String mobilityIntensity,

        @Schema(description = EmployabilityGapResourceDocs.GAP_SEVERITY)
        String gapSeverity,

        @Schema(description = EmployabilityGapResourceDocs.GAP_SCORE)
        double gapScore,

        @Schema(description = EmployabilityGapResourceDocs.PRIMARY_FACTORS)
        List<String> primaryFactors
) {
}
