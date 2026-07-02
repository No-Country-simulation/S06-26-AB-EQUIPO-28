package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.AuthenticationResourceDocs;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.UserResourceDocs;

import java.util.List;
import java.util.UUID;

/**
 * Recurso de respuesta tras registrar un nuevo usuario exitosamente.
 * Nota: El token ya no se devuelve en el registro - el usuario debe hacer login.
 */
@Schema(description = AuthenticationResourceDocs.REGISTERED_USER_RESOURCE)
public record RegisteredUserResource(
        @Schema(description = UserResourceDocs.USER_ID)
        UUID userId,

        @Schema(description = UserResourceDocs.USERNAME)
        String username,

        @Schema(description = UserResourceDocs.ROLES)
        List<Roles> roles,

        @Schema(description = UserResourceDocs.IS_ACTIVE)
        boolean isActive
) { }