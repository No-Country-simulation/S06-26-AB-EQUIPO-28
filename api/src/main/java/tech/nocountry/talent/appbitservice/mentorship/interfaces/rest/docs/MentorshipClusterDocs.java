package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipClusterSummaryResource;

import java.util.List;

/**
 * OpenAPI contract for Mentorship Cluster Summary endpoints.
 */
@Tag(name = "Mentorship - Clusters", description = "API para resumen de programas de mentoría por cluster/región")
public interface MentorshipClusterDocs {

    @Operation(
            summary = "Listar clusters con programas de mentoría",
            description = """
                    Enumera todos los clusters geográficos que tienen programas de mentoría registrados, \
                    proporcionando estadísticas agregadas (conteos, capacidades, modalidades, públicos \
                    objetivo) por cluster. Filtrable opcionalmente por área de enfoque."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de resúmenes por cluster",
                    content = @Content(schema = @Schema(implementation = MentorshipClusterSummaryResource.class))
            )
    })
    ResponseEntity<List<MentorshipClusterSummaryResource>> getClusters(
            @Parameter(description = "Filtrar por área de enfoque: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL.")
            @RequestParam(required = false) String focusArea
    );
}