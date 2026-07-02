package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.AuthenticationResourceDocs;

/** Recurso de solicitud de inicio de sesión. */
@Schema(description = AuthenticationResourceDocs.SIGN_IN_RESOURCE)
public record SignInResource(
        @NotBlank
        @Size(min = 3, max = 100)
        @Schema(description = AuthenticationResourceDocs.USERNAME, minLength = 3, maxLength = 100)
        String username,

        @NotBlank
        @Size(min = 8, max = 200)
        @Schema(description = AuthenticationResourceDocs.PASSWORD, minLength = 8, maxLength = 200)
        String password
) { }