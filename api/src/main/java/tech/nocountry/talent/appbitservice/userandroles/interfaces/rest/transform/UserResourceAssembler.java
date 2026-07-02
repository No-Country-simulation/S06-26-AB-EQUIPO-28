package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.*;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserSummary;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserResourceAssembler {
    @Mapping(target = "userId", expression = "java(user.getUserId())")
    @Mapping(target = "username", expression = "java(user.getUsername().getValue())")
    @Mapping(target = "roles", expression = "java(mapToRoles(user.getRolesAsStrings()))")
    @Mapping(target = "isActive", expression = "java(user.isActive())")
    UserResource toResource(User user);

    UserResource toResource(UserSummary summary);

    /** Convierte lista de strings de roles a lista de Roles. */
    default List<Roles> mapToRoles(List<String> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(Roles::valueOf)
                .toList();
    }
}