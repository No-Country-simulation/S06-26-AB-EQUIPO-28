package tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources.MobilityODPairResourceDocs;

import java.time.Instant;

/**
 * REST resource for mobility OD pairs.
 *
 * <p>DTO que representa un par origen-destino de movilidad del dataset CDRView.
 * Usa {@code @Schema} con descripciones reutilizables desde
 * {@link MobilityODPairResourceDocs}.</p>
 */
@Schema(description = MobilityODPairResourceDocs.DESCRIPTION)
public record MobilityODPairResource(
        @Schema(description = MobilityODPairResourceDocs.ORIGIN_CLUSTER)
        String originCluster,

        @Schema(description = MobilityODPairResourceDocs.ORIGIN_MUNICIPIO)
        String originMunicipio,

        @Schema(description = MobilityODPairResourceDocs.ORIGIN_LATITUDE)
        double originLatitude,

        @Schema(description = MobilityODPairResourceDocs.ORIGIN_LONGITUDE)
        double originLongitude,

        @Schema(description = MobilityODPairResourceDocs.DESTINATION_CLUSTER)
        String destinationCluster,

        @Schema(description = MobilityODPairResourceDocs.DESTINATION_MUNICIPIO)
        String destinationMunicipio,

        @Schema(description = MobilityODPairResourceDocs.DESTINATION_LATITUDE)
        double destinationLatitude,

        @Schema(description = MobilityODPairResourceDocs.DESTINATION_LONGITUDE)
        double destinationLongitude,

        @Schema(description = MobilityODPairResourceDocs.SAME_CLUSTER)
        boolean sameCluster,

        @Schema(description = MobilityODPairResourceDocs.UNIQUE_USERS)
        int uniqueUsers,

        @Schema(description = MobilityODPairResourceDocs.TOTAL_TRIPS)
        int totalTrips,

        @Schema(description = MobilityODPairResourceDocs.AVERAGE_DISTANCE_KM)
        double averageDistanceKm,

        @Schema(description = MobilityODPairResourceDocs.PREDOMINANT_PERIOD)
        String predominantPeriod,

        @Schema(description = MobilityODPairResourceDocs.CREATED_AT)
        Instant createdAt,

        @Schema(description = MobilityODPairResourceDocs.UPDATED_AT)
        Instant updatedAt
) {
}
