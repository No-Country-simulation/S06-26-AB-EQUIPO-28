package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.UpdateUserCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UpdateUserResource;

import java.util.List;
import java.util.UUID;

/** Ensamblador para convertir UpdateUserResource a UpdateUserCommand. */
public class UpdateUserCommandFromEntityAssembler {
    /**
     * Convierte un UpdateUserResource a UpdateUserCommand.
     * 
     * @param userId el ID del usuario
     * @param userResource el recurso con los datos de actualización
     * @return el comando de actualización
     */
    public static UpdateUserCommand toCommandFromResource(UUID userId, UpdateUserResource userResource) {
        return new UpdateUserCommand(
            userId, 
            userResource.userName(), 
            userResource.password(), 
            userResource.isActive(),
            mapRoles(userResource.roles())
        );
    }

    /** Convierte lista de Roles a lista de strings para el comando. */
    private static List<String> mapRoles(List<Roles> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Roles::name)
                .toList();
    }
}
