package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.*;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SignUpCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignUpResource;

import java.util.List;

/** Ensamblador MapStruct para convertir SignUpResource a SignUpCommand. */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SignUpCommandFromResourceAssembler {

    /** Convierte SignUpResource a SignUpCommand mapeando roles de Roles a Role. */
    @Mapping(source = "userName", target = "username")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToRoles")
    @Mapping(target = "userId", expression = "java(java.util.Optional.empty())")
    SignUpCommand toCommandFromResource(SignUpResource resource);

    /** Mapea Boolean a boolean, retornando true por defecto si es null. */
    default boolean mapIsActive(Boolean isActive) {
        return isActive != null ? isActive : true;
    }

    /** Rol por defecto si no se proporcionan roles. */
    default List<Role> defaultRoles() {
        return List.of(Role.create(Roles.GENERAL_USER));
    }

    /** Convierte lista de Roles a lista de Roles. */
    @Named("rolesToRoles")
    default List<Role> mapRolesToRoles(List<Roles> roles) {
        if (roles == null || roles.isEmpty()) {
            return defaultRoles();
        }
        return roles.stream()
                .map(role -> Roles.valueOf(role.name()))
                .map(Role::create)
                .toList();
    }
}
