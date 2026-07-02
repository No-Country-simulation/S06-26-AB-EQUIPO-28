package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.AuthenticatedUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RegisteredUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignInResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignUpResource;

@Tag(name = "Authentication", description = "Registro e inicio de sesión")
@Validated
public interface AuthenticationDocs {
    @Operation(summary = "Inicia sesión")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión iniciada",
            content = @Content(schema = @Schema(implementation = AuthenticatedUserResource.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Usuario inactivo",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<AuthenticatedUserResource> signIn(
        @Valid @RequestBody(content = @Content(schema = @Schema(implementation = SignInResource.class)))
        SignInResource signInResource
    );

    @Operation(summary = "Registra un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = RegisteredUserResource.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "El usuario ya existe",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<RegisteredUserResource> signUp(
        @Valid @RequestBody(content = @Content(schema = @Schema(implementation = SignUpResource.class)))
        SignUpResource signUpResource
    );

    //-----------------------------------------------
}
