package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates.CitizenProfile;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenPaginatedResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;

import java.util.List;

/**
 * Assembler MapStruct para transformar CitizenProfile (entidad de dominio) a CitizenResource (REST DTO).
 *
 * <p>Mapea automáticamente los Value Objects anidados extrayendo su valor mediante source="field.value".</p>
 */
@Mapper(componentModel = "spring")
public interface DemographicsResourceAssembler {

    /**
     * Transforma un CitizenProfile a su correspondiente recurso REST.
     *
     * @param citizenProfile la entidad de dominio
     * @return el recurso REST
     */
    @Mapping(target = "citizenHash", source = "citizenId.value")
    @Mapping(target = "incomeLevel", source = "incomeLevel.value")
    @Mapping(target = "ageGroup", source = "ageGroup.value")
    @Mapping(target = "mobilityPattern", source = "mobilityPattern.value")
    @Mapping(target = "homeCluster", source = "homeCluster.value")
    CitizenResource toResource(CitizenProfile citizenProfile);

    /**
     * Transforma una lista de CitizenProfile a lista de recursos REST.
     *
     * @param citizens la lista de entidades de dominio
     * @return la lista de recursos REST
     */
    List<CitizenResource> toResourceList(List<CitizenProfile> citizens);

    /**
     * Convierte una página de CitizenProfile a un recurso paginado.
     *
     * @param page la página de perfiles de ciudadanos desde el use case
     * @return el recurso paginado con la lista y metadatos de paginación
     */
    default CitizenPaginatedResource toPaginatedResource(Page<CitizenProfile> page) {
        return new CitizenPaginatedResource(
                page.getContent().stream()
                        .map(this::toResource)
                        .toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
