package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.*;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SignUpCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RegisteredUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignUpResource;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RegisteredUserResourceAssembler {
    @Mapping(target = "userId", expression = "java(getUserIdOrNull(command))")
    @Mapping(target = "username", source = "command.username")
    @Mapping(target = "roles", expression = "java(getRolesAsStrings(command))")
    @Mapping(target = "isActive", source = "command.isActive")
    RegisteredUserResource toResource(SignUpCommand command);

    default UUID getUserIdOrNull(SignUpCommand command) {
        return command.userId().orElse(null);
    }

    default List<Roles> getRolesAsStrings(SignUpCommand command) {
        if (command.roles() == null) {
            return List.of(Roles.GENERAL_USER);
        }
        return command.roles().stream()
                .map(Role::getStringName)
                .map(Roles::valueOf)
                .toList();
    }

    default List<Roles> getRolesAsStringsFromResource(SignUpResource resource) {
        if (resource.roles() == null || resource.roles().isEmpty()) {
            return List.of(Roles.GENERAL_USER);
        }
        return resource.roles();
    }

    default RegisteredUserResource toResource(SignUpResource resource, UUID userId) {
        return new RegisteredUserResource(
                userId,
                resource.userName(),
                getRolesAsStringsFromResource(resource),
                resource.isActive()
        );
    }
}