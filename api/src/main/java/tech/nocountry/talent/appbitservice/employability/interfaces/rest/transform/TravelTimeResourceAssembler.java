package tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.TravelTime;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimePaginatedResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimeResource;

/**
 * MapStruct assembler que transforma {@link TravelTime} aggregate roots en
 * REST resources.
 *
 * <p>Los clusters y {@code predominantPeriod} se persisten como {@code String}
 * directo (no como VOs embebidos), por lo que se mapean campo a campo. Los
 * percentiles {@code p25DistanceKm} y {@code p75DistanceKm} son nullable
 * ({@code Double}) y se mapean directamente.</p>
 */
@Mapper(componentModel = "spring")
public interface TravelTimeResourceAssembler {

    /**
     * Mapea un agregado completo a un resource REST.
     *
     * @param travelTime el aggregate root
     * @return el resource DTO completo
     */
    TravelTimeResource toResource(TravelTime travelTime);

    /**
     * Transforma una {@link Page} de agregados en un resource paginado.
     *
     * <p>Método default MapStruct: mapea cada agregado a un
     * {@link TravelTimeResource} y envuelve la lista con los metadatos de
     * paginación.</p>
     *
     * @param page la página de agregados de dominio
     * @return el resource REST paginado
     */
    default TravelTimePaginatedResource toPaginatedResource(Page<TravelTime> page) {
        return new TravelTimePaginatedResource(
                page.getContent().stream().map(this::toResource).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
