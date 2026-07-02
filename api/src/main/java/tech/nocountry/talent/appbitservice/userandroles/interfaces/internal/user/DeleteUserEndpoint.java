package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases.DeleteUserCommandUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.DeleteUserCommand;

import java.util.UUID;

/**
 * Endpoint interno para eliminar un usuario.
 * Transforma el ID en un comando y delega al caso de uso.
 */
@Component
@RequiredArgsConstructor
public class DeleteUserEndpoint {
    private final DeleteUserCommandUseCase deleteUserUseCase;

    /**
     * Elimina un usuario existente.
     *
     * @param userId ID del usuario a eliminar
     */
    public void handle(UUID userId) {
        var command = new DeleteUserCommand(userId);
        deleteUserUseCase.handle(command);
    }
}