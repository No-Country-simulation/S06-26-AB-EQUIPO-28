package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserSummaryResource;

import java.util.List;

/** Ensamblador MapStruct para convertir UserSummaryResource a UserResource. */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserResourceFromProjectionAssembler {

    /**
     * Convierte un UserSummaryResource a UserResource.
     * 
     * @param projection la proyección del usuario
     * @return el recurso del usuario
     */
    default UserResource toResource(UserSummaryResource projection) {
        return new UserResource(
            projection.userId(),
            projection.username(),
            mapRoles(projection.roles()),
            projection.isActive()
        );
    }

    /** Convierte lista de strings de roles a lista de Roles. */
    default List<Roles> mapRoles(List<String> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Roles::valueOf)
                .toList();
    }

    /**
     * Convierte una lista de UserSummaryResources a una lista de UserResources.
     * 
     * @param projections la lista de proyecciones de usuario
     * @return la lista de recursos de usuario
     */
    default List<UserResource> toResourceList(List<UserSummaryResource> projections) {
        return projections.stream()
            .map(this::toResource)
            .toList();
    }
}
