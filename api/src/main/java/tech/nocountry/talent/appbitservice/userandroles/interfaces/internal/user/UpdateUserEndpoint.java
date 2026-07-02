package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases.UpdateUserCommandUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserSummary;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UpdateUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.UpdateUserCommandFromEntityAssembler;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.UserResourceAssembler;

import java.util.UUID;

/**
 * Endpoint interno para actualizar un usuario.
 * Transforma el recurso de entrada, ejecuta el caso de uso de comando y transforma la respuesta.
 */
@Component
@RequiredArgsConstructor
public class UpdateUserEndpoint {
    private final UpdateUserCommandUseCase updateUserUseCase;
    private final UserResourceAssembler userResourceAssembler;

    /**
     * Actualiza un usuario existente.
     *
     * @param userId ID del usuario a actualizar
     * @param resource datos de actualización del usuario
     * @return recurso con los datos actualizados del usuario
     */
    public UserResource handle(UUID userId, UpdateUserResource resource) {
        var command = UpdateUserCommandFromEntityAssembler.toCommandFromResource(userId, resource);
        UserSummary userSummary = updateUserUseCase.handle(command);
        return userResourceAssembler.toResource(userSummary);
    }
}