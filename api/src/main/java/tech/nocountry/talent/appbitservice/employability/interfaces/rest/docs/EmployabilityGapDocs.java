package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs;

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
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.EmployabilityGapResource;

import java.util.List;

/**
 * OpenAPI contract para los endpoints de análisis de brechas de empleabilidad.
 */
@Tag(name = "Employability - Gaps", description = "API para análisis de brechas de empleabilidad por cluster geográfico")
public interface EmployabilityGapDocs {

    @Operation(
            summary = "Analizar brechas de empleabilidad",
            description = """
                    Cruza la matriz OD de movilidad de este BC con los índices demográficos \
                    (ACL demographics) y la telemetría diurna (ACL telemetry) para identificar \
                    clusters con alta densidad de ciudadanos en horario laboral pero baja \
                    conectividad saliente hacia los hubs de empleo. Los resultados se ordenan \
                    por gapScore descendente y se clasifican como CRITICAL, HIGH, MODERATE, \
                    LOW o NONE."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultados del análisis de brechas de empleabilidad",
                    content = @Content(schema = @Schema(implementation = EmployabilityGapResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Datos de demografía o telemetría no disponibles",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<List<EmployabilityGapResource>> getGaps(
            @Parameter(description = "Número máximo de resultados (1-1000, default: 20)", example = "20")
            @RequestParam(defaultValue = "20") int maxResults,

            @Parameter(description = "Severidad mínima de la brecha: CRITICAL, HIGH, MODERATE, LOW, NONE. Opcional (null = todas).")
            @RequestParam(required = false) String minSeverity,

            @Parameter(description = "Filtrar por cluster específico. Opcional.")
            @RequestParam(required = false) String cluster,

            @Parameter(description = "Si es true, solo se devuelven clusters sin cobertura de telemetría (blind zones).")
            @RequestParam(defaultValue = "false") boolean onlyBlindZones
    );
}
