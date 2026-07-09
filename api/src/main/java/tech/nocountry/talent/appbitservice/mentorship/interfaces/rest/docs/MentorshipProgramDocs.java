package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramPaginatedResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;

/**
 * OpenAPI contract for Mentorship Program endpoints.
 */
@Tag(name = "Mentorship - Programs", description = "API para gestión de programas de mentoría pública")
public interface MentorshipProgramDocs {

    @Operation(
            summary = "Listar programas de mentoría",
            description = "Recupera una lista paginada y filtrada de programas de mentoría del catálogo público."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de programas paginada",
                    content = @Content(schema = @Schema(implementation = MentorshipProgramPaginatedResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de paginación o filtro inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<MentorshipProgramPaginatedResource> getPrograms(
            @Parameter(description = "Número de página (empieza en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página (1-100)", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Criterio de ordenamiento: campo,dirección. Ejemplo: createdAt,desc", example = "createdAt,desc")
            @RequestParam(defaultValue = "createdAt,desc") String sort,

            @Parameter(description = "Filtrar por área de enfoque: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL. Opcional.")
            @RequestParam(required = false) String focusArea,

            @Parameter(description = "Filtrar por modalidad: REMOTE, IN_PERSON, HYBRID. Opcional.")
            @RequestParam(required = false) String modality,

            @Parameter(description = "Filtrar por nombre de cluster geográfico. Opcional.")
            @RequestParam(required = false) String clusterName,

            @Parameter(description = "Filtrar por nivel de ingreso objetivo. Opcional.")
            @RequestParam(required = false) String targetIncomeLevel,

            @Parameter(description = "Filtrar por estado activo. Opcional (null = todos).")
            @RequestParam(required = false) Boolean isActive
    );

    @Operation(
            summary = "Obtener programa por ID",
            description = "Recupera un único programa de mentoría mediante su identificador de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Programa encontrado",
                    content = @Content(schema = @Schema(implementation = MentorshipProgramResource.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Programa no encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<MentorshipProgramResource> getProgramByProgramId(
            @Parameter(description = "Identificador de negocio del programa (ej: MPR-001)", example = "MPR-001")
            @PathVariable String programId
    );

    @Operation(
            summary = "Crear programa",
            description = "Crea un nuevo programa de mentoría en el catálogo público."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Programa creado correctamente. La cabecera Location apunta al nuevo recurso.",
                    content = @Content(schema = @Schema(implementation = MentorshipProgramResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Entrada inválida o identificador de programa duplicado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<MentorshipProgramResource> createProgram(
            @Valid
            @RequestBody(
                    description = "Datos para la creación del programa",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateMentorshipProgramCommand.class))
            )
            CreateMentorshipProgramCommand command
    );
}