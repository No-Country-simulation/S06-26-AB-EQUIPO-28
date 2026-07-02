package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.RoleResourceDocs;

import java.util.UUID;

/** Recurso de rol para respuestas REST. */
@Schema(description = RoleResourceDocs.ROLE_RESOURCE)
public record RoleResource(
        @Schema(description = RoleResourceDocs.ROLE_ID)
        UUID roleId,

        @Schema(description = RoleResourceDocs.NAME, implementation = Roles.class)
        String name
) { }