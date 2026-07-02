package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.UserResourceDocs;

import java.util.List;

/** Recurso para actualizar un usuario. */
@Schema(description = UserResourceDocs.UPDATE_USER_RESOURCE)
public record UpdateUserResource(
        @Size(max = 100)
        @JsonProperty("username")
        @Schema(description = UserResourceDocs.USERNAME, maxLength = 100)
        String userName,

        @Size(min = 8, max = 200)
        @JsonProperty("password")
        @Schema(description = UserResourceDocs.PASSWORD, minLength = 8, maxLength = 200)
        String password,

        @JsonProperty("isActive")
        @Schema(description = UserResourceDocs.IS_ACTIVE, nullable = true)
        Boolean isActive,

        @JsonProperty("roles")
        @Schema(description = UserResourceDocs.ROLES_OPTIONAL)
        List<Roles> roles
) { }