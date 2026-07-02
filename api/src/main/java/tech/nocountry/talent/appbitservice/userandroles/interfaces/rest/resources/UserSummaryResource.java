package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.UserResourceDocs;

import java.util.List;
import java.util.UUID;

/** Recurso resumido de usuario para listados. */
@Schema(description = UserResourceDocs.USER_SUMMARY_RESOURCE)
public record UserSummaryResource(
        @Schema(description = UserResourceDocs.USER_ID)
        UUID userId,

        @Schema(description = UserResourceDocs.USERNAME)
        String username,

        @Schema(description = UserResourceDocs.ROLES, implementation = Roles.class)
        List<String> roles,

        @Schema(description = UserResourceDocs.IS_ACTIVE)
        boolean isActive
) { }