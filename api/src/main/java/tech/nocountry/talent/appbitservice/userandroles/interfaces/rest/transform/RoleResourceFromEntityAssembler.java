package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RoleResource;

import java.util.List;

/** Ensamblador MapStruct para convertir Role a RoleResource. */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleResourceFromEntityAssembler {
    /** Convierte una entidad Role a RoleResource. */
    RoleResource toResource(Role role);

    /** Convierte una lista de entidades Role a una lista de RoleResources. */
    List<RoleResource> toResourceList(List<Role> roles);
}
