package tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases.GetConcentrationFilteredQueryUseCase;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.queries.GetConcentrationFilteredQuery;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform.ConcentrationResourceAssembler;

import java.time.LocalDate;

/**
 * Endpoint interno para obtener métricas de concentración con filtros y paginación.
 *
 * <p>Este es un endpoint interno ({@code @Component}) que construye el query,
 * ejecuta el caso de uso y transforma el resultado paginado a Resource.
 * Consumido por el Controller REST. No es un REST Controller.</p>
 *
 * <p>Sigue el patrón de Gastro Suite: el endpoint usa el Assembler para transformar
 * entidades del dominio a Resources listos para la capa de presentación.</p>
 */
@Component
@RequiredArgsConstructor
public class GetConcentrationFilteredEndpoint {
    private final GetConcentrationFilteredQueryUseCase useCase;
    private final ConcentrationResourceAssembler assembler;

    /**
     * Obtiene las métricas de concentración aplicando filtros opcionales y paginación.
     *
     * @param cluster   nombre del cluster geográfico (opcional)
     * @param startDate fecha inicial del rango (opcional)
     * @param endDate   fecha final del rango (opcional)
     * @param period    período de sesión (opcional)
     * @param page      número de página (0-based)
     * @param size      tamaño de página
     * @return recurso paginado de concentración
     */
    public ConcentrationPaginatedResource handle(
            String cluster, LocalDate startDate, LocalDate endDate,
            String period, int page, int size) {
        var query = new GetConcentrationFilteredQuery(cluster, startDate, endDate, period, null, page, size);
        var result = useCase.handle(query);
        return assembler.toPaginatedResource(result);
    }

    /**
     * Obtiene todas las métricas de concentración sin límite de paginación.
     *
     * @return recurso paginado con todos los resultados
     */
    public ConcentrationPaginatedResource handleAll() {
        var query = new GetConcentrationFilteredQuery(null, null, null, null, null, 0, Integer.MAX_VALUE);
        var result = useCase.handle(query);
        return assembler.toPaginatedResource(result);
    }

    /**
     * Obtiene todas las métricas de concentración para un cluster sin límite de paginación.
     *
     * @param cluster nombre del cluster geográfico
     * @return recurso paginado con todos los resultados del cluster
     */
    public ConcentrationPaginatedResource handleAll(String cluster) {
        var query = new GetConcentrationFilteredQuery(cluster, null, null, null, null, 0, Integer.MAX_VALUE);
        var result = useCase.handle(query);
        return assembler.toPaginatedResource(result);
    }
}
