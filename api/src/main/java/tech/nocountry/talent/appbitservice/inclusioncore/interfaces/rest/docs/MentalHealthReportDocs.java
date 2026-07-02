package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.MentalHealthReportResource;

/**
 * OpenAPI contract for the Mental Health Report endpoints.
 *
 * <p>Defines the REST operations for querying mental health vulnerability
 * reports for government managers.</p>
 */
@Tag(name = "Inclusion - Mental Health", description = "API para reportes de vulnerabilidad en salud mental")
public interface MentalHealthReportDocs {
    @Operation(
            summary = "Obtener reporte de salud mental",
            description = """
                    Genera un reporte ejecutivo de vulnerabilidad en salud mental por región para el
                    período indicado. Los indicadores se calculan al vuelo cruzando datos demográficos
                    (proporción de población de renta baja) con telemetría de red (cobertura y calidad de
                    conectividad).

                    Premisa de producto: sin conectividad no hay soporte remoto de salud mental, por lo que
                    las regiones con peor red y mayor vulnerabilidad social son las prioritarias. El reporte
                    ayuda a los gestores públicos a decidir dónde intervenir primero.

                    Contenido del reporte:
                    - regionSummaries: lista de regiones con su score y nivel de vulnerabilidad.
                    - metadata: agregados (población total y vulnerable, score promedio, nº de regiones prioritarias).
                    - reportId, generatedAt y reportPeriod: identificación y trazabilidad del reporte.

                    Si se indica includePriorityOnly=true, solo se incluyen las regiones prioritarias.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reporte de salud mental generado exitosamente",
                    content = @Content(schema = @Schema(implementation = MentalHealthReportResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos (p. ej. reportPeriod con formato distinto de YYYY-Qn)",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al generar el reporte",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    ResponseEntity<MentalHealthReportResource> getMentalHealthReport(
            @Parameter(
                    description = "Período del reporte en formato trimestral YYYY-Qn (ej. 2026-Q2). "
                            + "Si se omite, se usa el trimestre actual.",
                    example = "2026-Q2")
            @RequestParam(required = false) String reportPeriod,
            @Parameter(
                    description = "Si es true, el reporte incluye únicamente las regiones prioritarias "
                            + "(alta vulnerabilidad y conectividad LOW). Si es false, incluye todas las regiones.",
                    example = "false")
            @RequestParam(defaultValue = "false") boolean includePriorityOnly
    );
}
