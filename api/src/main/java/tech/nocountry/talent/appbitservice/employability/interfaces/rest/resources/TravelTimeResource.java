package tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources.TravelTimeResourceDocs;

/**
 * REST resource for inter-cluster travel times.
 *
 * <p>DTO que representa un registro de tiempos de viaje inter-cluster del dataset
 * CDRView. Usa {@code @Schema} con descripciones reutilizables desde
 * {@link TravelTimeResourceDocs}.</p>
 */
@Schema(description = TravelTimeResourceDocs.DESCRIPTION)
public record TravelTimeResource(
        @Schema(description = TravelTimeResourceDocs.ORIGIN_CLUSTER)
        String originCluster,

        @Schema(description = TravelTimeResourceDocs.DESTINATION_CLUSTER)
        String destinationCluster,

        @Schema(description = TravelTimeResourceDocs.SAME_CLUSTER)
        boolean sameCluster,

        @Schema(description = TravelTimeResourceDocs.OBSERVATIONS)
        int observations,

        @Schema(description = TravelTimeResourceDocs.AVERAGE_DISTANCE_KM)
        double averageDistanceKm,

        @Schema(description = TravelTimeResourceDocs.P25_DISTANCE_KM)
        Double p25DistanceKm,

        @Schema(description = TravelTimeResourceDocs.P75_DISTANCE_KM)
        Double p75DistanceKm,

        @Schema(description = TravelTimeResourceDocs.PREDOMINANT_PERIOD)
        String predominantPeriod
) {
}
