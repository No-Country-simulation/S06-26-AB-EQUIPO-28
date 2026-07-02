package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases.SignUpCommandUseCase;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RegisteredUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignUpResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.RegisteredUserResourceAssembler;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.SignUpCommandFromResourceAssembler;

/**
 * Endpoint interno para registrar un nuevo usuario.
 * Transforma el recurso de entrada, ejecuta el caso de uso y transforma la respuesta.
 */
@Component
@RequiredArgsConstructor
public class SignUpEndpoint {
    private final SignUpCommandUseCase signUpUseCase;
    private final SignUpCommandFromResourceAssembler signUpAssembler;
    private final RegisteredUserResourceAssembler registeredUserAssembler;

    /**
     * Registra un nuevo usuario.
     *
     * @param resource datos del nuevo usuario
     * @return recurso con los datos del usuario registrado
     */
    public RegisteredUserResource handle(SignUpResource resource) {
        var command = signUpAssembler.toCommandFromResource(resource);
        var userId = signUpUseCase.handle(command);
        return registeredUserAssembler.toResource(resource, userId);
    }
}