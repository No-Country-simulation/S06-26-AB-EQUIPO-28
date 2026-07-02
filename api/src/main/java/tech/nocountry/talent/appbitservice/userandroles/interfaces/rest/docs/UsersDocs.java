package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UpdateUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserPaginatedResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;

import java.util.UUID;

@Tag(name = "Users", description = "Gestión de usuarios")
public interface UsersDocs {

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
            content = @Content(schema = @Schema(implementation = UserResource.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<UserResource> updateUser(
        @Parameter(description = "UUID del usuario") @PathVariable UUID userId,
        @Valid @RequestBody UpdateUserResource userResource
    );

    //-----------------------------------------------

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Eliminar usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "400", description = "No puedes eliminarte a ti mismo",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<?> deleteUser(@Parameter(description = "UUID del usuario") @PathVariable UUID userId);

    //-----------------------------------------------

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios",
                    content = @Content(schema = @Schema(implementation = UserPaginatedResource.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<?> getUsers(
            @Min(0) @Parameter(description = "Número de página (inicia en 0). Si no se envía, retorna todos.") @RequestParam(required = false) Integer page,
            @Min(1) @Max(100) @Parameter(description = "Cantidad de registros por página (máx. 100). Si no se envía, retorna todos.") @RequestParam(required = false) Integer size,
            @Parameter(description = "Buscar por userId, nombre de usuario y roles") @RequestParam(required = false) String search
    );

    //-----------------------------------------------

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<UserResource> getUserById(@Parameter(description = "UUID del usuario") @PathVariable UUID userId);

    //-----------------------------------------------
}
