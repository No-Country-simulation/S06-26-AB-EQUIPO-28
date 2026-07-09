package tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases.GetTravelTimesUseCase;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetTravelTimesQuery;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimePaginatedResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform.TravelTimeResourceAssembler;

/**
 * Internal endpoint para operaciones de consulta de tiempos de viaje
 * inter-cluster.
 *
 * <p>Orquesta el use case y transforma los resultados de dominio en REST
 * resources usando el assembler.</p>
 */
@Component
@RequiredArgsConstructor
public class TravelTimeInternalEndpoint {

    private final GetTravelTimesUseCase getTravelTimesUseCase;
    private final TravelTimeResourceAssembler assembler;

    /**
     * Recupera una lista paginada de tiempos de viaje con filtros opcionales.
     *
     * @param query la query paginada
     * @return los resources de tiempos de viaje paginados
     */
    public TravelTimePaginatedResource getTravelTimes(GetTravelTimesQuery query) {
        var result = getTravelTimesUseCase.execute(query);
        return assembler.toPaginatedResource(result);
    }
}
