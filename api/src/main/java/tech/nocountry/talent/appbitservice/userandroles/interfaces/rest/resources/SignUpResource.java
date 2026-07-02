package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.AuthenticationResourceDocs;

import java.util.List;

/** Recurso de solicitud de registro de usuario. */
@Schema(description = AuthenticationResourceDocs.SIGN_UP_RESOURCE)
public record SignUpResource(
        @NotBlank
        @Size(min = 3, max = 100)
        @JsonProperty("username")
        @Schema(description = AuthenticationResourceDocs.SIGN_UP_USERNAME, minLength = 3, maxLength = 100)
        String userName,

        @NotBlank
        @Size(min = 8, max = 200)
        @Schema(description = AuthenticationResourceDocs.SIGN_UP_PASSWORD, minLength = 8, maxLength = 200)
        String password,

        @NotNull
        @Schema(description = AuthenticationResourceDocs.IS_ACTIVE)
        Boolean isActive,

        @NotNull
        @Size(min = 1)
        @Schema(description = AuthenticationResourceDocs.SIGN_UP_ROLES)
        List<Roles> roles
) { }