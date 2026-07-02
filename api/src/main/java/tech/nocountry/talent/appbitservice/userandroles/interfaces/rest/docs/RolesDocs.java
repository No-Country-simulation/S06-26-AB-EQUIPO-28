package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RoleResource;

import java.util.List;

@Tag(name = "Roles", description = "Ver roles disponibles")
public interface RolesDocs {

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lista roles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles",
            content = @Content(schema = @Schema(type = "array", implementation = RoleResource.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<List<RoleResource>> getAllRoles();

    //-----------------------------------------------
}
