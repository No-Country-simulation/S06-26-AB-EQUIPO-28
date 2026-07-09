package tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases.GetMobilityODPairsUseCase;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetMobilityODPairsQuery;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairPaginatedResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform.MobilityODPairResourceAssembler;

/**
 * Internal endpoint para operaciones de consulta de pares OD de movilidad.
 *
 * <p>Orquesta el use case y transforma los resultados de dominio en REST
 * resources usando el assembler. Sigue el patrón Gastro Suite: el controller
 * REST solo delega a este internal endpoint; la transformación real vive
 * aquí.</p>
 */
@Component
@RequiredArgsConstructor
public class MobilityODPairInternalEndpoint {

    private final GetMobilityODPairsUseCase getODPairsUseCase;
    private final MobilityODPairResourceAssembler assembler;

    /**
     * Recupera una lista paginada de pares OD con filtros opcionales.
     *
     * <p>Construye el wrapper {@link MobilityODPairPaginatedResource} a partir
     * del {@link org.springframework.data.domain.Page} retornado por el use
     * case, siguiendo el patrón de paginación del repositorio.</p>
     *
     * @param query la query paginada
     * @return los resources de pares OD paginados
     */
    public MobilityODPairPaginatedResource getODPairs(GetMobilityODPairsQuery query) {
        var result = getODPairsUseCase.execute(query);
        return assembler.toPaginatedResource(result);
    }
}
