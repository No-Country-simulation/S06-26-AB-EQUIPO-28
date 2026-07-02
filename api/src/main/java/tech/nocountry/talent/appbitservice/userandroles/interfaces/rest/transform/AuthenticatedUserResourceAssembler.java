package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mapstruct.*;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.AuthenticatedUserResource;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthenticatedUserResourceAssembler {

    @Mapping(target = "userId", expression = "java(authResult.getLeft().getUserId())")
    @Mapping(target = "username", expression = "java(authResult.getLeft().getUsername().getValue())")
    @Mapping(target = "token", expression = "java(authResult.getRight())")
    @Mapping(target = "roles", expression = "java(mapToRoles(authResult.getLeft().getRolesAsStrings()))")
    @Mapping(target = "isActive", expression = "java(authResult.getLeft().isActive())")
    AuthenticatedUserResource toResource(ImmutablePair<User, String> authResult);

    /** Convierte lista de strings de roles a lista de Roles. */
    default List<Roles> mapToRoles(List<String> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Roles::valueOf)
                .toList();
    }
}