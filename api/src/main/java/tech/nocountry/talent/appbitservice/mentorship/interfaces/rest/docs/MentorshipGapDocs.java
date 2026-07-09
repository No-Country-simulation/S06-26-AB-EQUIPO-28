package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipGapResource;

import java.util.List;

/**
 * OpenAPI contract for Mentorship Gap Analysis endpoints.
 */
@Tag(name = "Mentorship - Gaps", description = "API para análisis de brechas de mentoría por región")
public interface MentorshipGapDocs {

    @Operation(
            summary = "Analizar brechas de mentoría",
            description = """
                    Cruza los datos de vulnerabilidad del motor de inclusión con el catálogo de programas \
                    de mentoría para identificar clusters con alta vulnerabilidad social pero \
                    cobertura insuficiente de programas. Los resultados se ordenan por puntaje de \
                    vulnerabilidad descendente y se clasifican como CRITICAL, HIGH o MODERATE."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultados del análisis de brechas",
                    content = @Content(schema = @Schema(implementation = MentorshipGapResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Datos de inclusión no disponibles",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<List<MentorshipGapResource>> getGaps(
            @Parameter(description = "Puntaje mínimo de vulnerabilidad (0-100, default: 60)", example = "60")
            @RequestParam(defaultValue = "60") int minVulnerabilityScore,

            @Parameter(description = "Número máximo de resultados (1-1000, default: 20)", example = "20")
            @RequestParam(defaultValue = "20") int maxResults,

            @Parameter(description = "Filtrar brechas por área de enfoque específica: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL.")
            @RequestParam(required = false) String focusArea
    );
}