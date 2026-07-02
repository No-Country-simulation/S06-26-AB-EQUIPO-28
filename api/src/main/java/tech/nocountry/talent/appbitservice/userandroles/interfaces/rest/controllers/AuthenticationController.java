package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.authentication.SignInEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.authentication.SignUpEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.AuthenticationDocs;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.AuthenticatedUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RegisteredUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignInResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignUpResource;

/**
 * Controlador REST para autenticación de usuarios.
 * Delega a los Endpoints internos.
 */
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Profile("dev")
public class AuthenticationController implements AuthenticationDocs {
    private final SignInEndpoint signInEndpoint;
    private final SignUpEndpoint signUpEndpoint;

    /**
     * Inicia sesión de un usuario.
     *
     * @param signInResource el recurso con las credenciales de acceso
     * @return el recurso con los datos del usuario autenticado y el token JWT
     */
    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@Valid @RequestBody SignInResource signInResource) {
        AuthenticatedUserResource response = signInEndpoint.execute(signInResource);
        return ResponseEntity.ok(response);
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param signUpResource el recurso con los datos de registro
     * @return el recurso con los datos del usuario registrado
     */
    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<RegisteredUserResource> signUp(@Valid @RequestBody SignUpResource signUpResource) {
        RegisteredUserResource response = signUpEndpoint.handle(signUpResource);
        return ResponseEntity.status(201).body(response);
    }
}