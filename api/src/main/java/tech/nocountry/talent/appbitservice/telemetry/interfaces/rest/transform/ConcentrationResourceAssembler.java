package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.entities.NetworkConcentration;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Mapper para transformar entre entidades NetworkConcentration del dominio y recursos REST.
 *
 * <p>Utiliza MapStruct para mapeo automático entre objetos del dominio y DTOs.</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ConcentrationResourceAssembler {

    /**
     * Transforma una NetworkConcentration entity a su recurso correspondiente.
     *
     * @param concentration la entidad del dominio
     * @return el recurso REST
     */
    @Mapping(target = "ecgi", expression = "java(concentration.getEcgi().getValue())")
    @Mapping(target = "period", expression = "java(concentration.getSessionPeriod())")
    @Mapping(target = "userCount", expression = "java(concentration.getUserCount())")
    @Mapping(target = "sessionCount", expression = "java(concentration.getSessionCount())")
    @Mapping(target = "averageDurationS", expression = "java(concentration.getAverageDurationS())")
    @Mapping(target = "dropPct", expression = "java(concentration.getConcentrationMetrics() != null ? concentration.getConcentrationMetrics().dropPct() : null)")
    @Mapping(target = "congestionLevel", expression = "java(concentration.getConcentrationMetrics() != null ? concentration.getConcentrationMetrics().congestionLevel() : null)")
    @Mapping(target = "totalCalls", expression = "java(concentration.getTotalCalls())")
    @Mapping(target = "totalMessages", expression = "java(concentration.getTotalMessages())")
    ConcentrationResource toResource(NetworkConcentration concentration);

    /**
     * Transforma una lista de NetworkConcentration entities a una lista de recursos.
     *
     * @param concentrations la lista de entidades del dominio
     * @return la lista de recursos REST
     */
    List<ConcentrationResource> toResourceList(List<NetworkConcentration> concentrations);

    /**
     * Transforma un {@link Page} de NetworkConcentration a un recurso paginado.
     *
     * @param page la página de entidades del dominio
     * @return el recurso paginado REST
     */
    default ConcentrationPaginatedResource toPaginatedResource(Page<NetworkConcentration> page) {
        return new ConcentrationPaginatedResource(
                page.getContent().stream().map(this::toResource).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
