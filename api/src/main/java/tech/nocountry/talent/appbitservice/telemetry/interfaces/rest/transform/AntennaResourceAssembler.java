package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates.Antenna;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;

import java.util.List;

/**
 * Mapper para transformar entre entidades Antenna del dominio y recursos REST.
 *
 * <p>Utiliza MapStruct para mapeo automático entre objetos del dominio y DTOs.</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface AntennaResourceAssembler {

    /**
     * Transforma un Antenna aggregate a su recurso correspondiente.
     *
     * @param antenna la entidad del dominio
     * @return el recurso REST
     */
    @Mapping(target = "ecgi", expression = "java(antenna.getEcgi().getValue())")
    @Mapping(target = "cluster", expression = "java(antenna.getCluster().getValue())")
    @Mapping(target = "municipality", expression = "java(antenna.getMunicipality().getValue())")
    @Mapping(target = "latitude", expression = "java(antenna.getLatitude().getValue())")
    @Mapping(target = "longitude", expression = "java(antenna.getLongitude().getValue())")
    AntennaResource toResource(Antenna antenna);

    /**
     * Transforma una lista de Antenna aggregates a una lista de recursos.
     *
     * @param antennas la lista de entidades del dominio
     * @return la lista de recursos REST
     */
    List<AntennaResource> toResourceList(List<Antenna> antennas);

    /**
     * Transforma un {@link Page} de Antenna a un recurso paginado.
     *
     * @param page la página de entidades del dominio
     * @return el recurso paginado REST
     */
    default AntennaPaginatedResource toPaginatedResource(Page<Antenna> page) {
        return new AntennaPaginatedResource(
                page.getContent().stream().map(this::toResource).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
