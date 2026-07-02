package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases.SignInCommandUseCase;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.AuthenticatedUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignInResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.AuthenticatedUserResourceAssembler;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.SignInCommandFromResourceAssembler;

/**
 * Endpoint interno para autenticación de usuarios.
 * Maneja la lógica de transformación y ejecución del comando SignIn.
 */
@Component
@RequiredArgsConstructor
public class SignInEndpoint {
    private final SignInCommandUseCase signInUseCase;
    private final SignInCommandFromResourceAssembler signInAssembler;
    private final AuthenticatedUserResourceAssembler resourceAssembler;

    /**
     * Ejecuta el flujo de autenticación interna.
     *
     * @param resource el recurso con las credenciales de acceso
     * @return el recurso con los datos del usuario autenticado y el token JWT
     */
    public AuthenticatedUserResource execute(SignInResource resource) {
        var command = signInAssembler.toCommandFromResource(resource);
        var authResult = signInUseCase.handle(command);
        return resourceAssembler.toResource(authResult);
    }
}
