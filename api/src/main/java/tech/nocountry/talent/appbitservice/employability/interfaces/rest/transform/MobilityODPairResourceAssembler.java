package tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.MobilityODPair;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairPaginatedResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairResource;

/**
 * MapStruct assembler que transforma {@link MobilityODPair} aggregate roots en
 * REST resources.
 *
 * <p>Los clusters y {@code predominantPeriod} se persisten como {@code String}
 * directo (no como VOs embebidos), por lo que no requieren
 * {@code @Mapping(source = "x.value", ...)}: se mapean campo a campo. Los
 * timestamps {@code createdAt} y {@code updatedAt} provienen de
 * {@code AuditableAbstractAggregateRoot} y se mapean directamente.</p>
 */
@Mapper(componentModel = "spring")
public interface MobilityODPairResourceAssembler {

    /**
     * Mapea un agregado completo a un resource REST.
     *
     * @param odPair el aggregate root
     * @return el resource DTO completo
     */
    MobilityODPairResource toResource(MobilityODPair odPair);

    /**
     * Transforma una {@link Page} de agregados en un resource paginado.
     *
     * <p>Método default MapStruct: mapea cada agregado a un
     * {@link MobilityODPairResource} y envuelve la lista con los metadatos de
     * paginación, siguiendo el mismo contrato que
     * {@code MentorshipProgramResourceAssembler#toPaginatedResource}.</p>
     *
     * @param page la página de agregados de dominio
     * @return el resource REST paginado
     */
    default MobilityODPairPaginatedResource toPaginatedResource(Page<MobilityODPair> page) {
        return new MobilityODPairPaginatedResource(
                page.getContent().stream().map(this::toResource).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
