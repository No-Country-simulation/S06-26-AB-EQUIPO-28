package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationPaginatedResource;

import java.time.LocalDate;

/**
 * Interfaz de documentación OpenAPI para el contexto de concentración de red.
 *
 * <p>Contiene las anotaciones de OpenAPI en español para documentar los endpoints
 * del recurso de métricas de concentración de red.</p>
 */
@Tag(name = "Concentration", description = "API de consulta de métricas de concentración de red, "
        + "agregadas por antena (ECGI), día y período del día. Permiten analizar volumen de usuarios, "
        + "tráfico de datos y calidad de red para los estudios de inclusión digital.")
public interface ConcentrationDocs {

    /**
     * Obtiene las métricas de concentración con filtros opcionales y paginación.
     */
    @Operation(
            summary = "Listar métricas de concentración",
            description = """
                    Retorna las métricas de concentración de red de forma paginada.
                    Todos los filtros son opcionales y se combinan con AND:
                      - cluster: zona geográfica de movilidad (coincidencia exacta).
                      - startDate / endDate: rango de fechas (formato ISO YYYY-MM-DD), inclusivo.
                      - period: franja horaria (DAWN, MORNING, AFTERNOON, NIGHT; case-insensitive).
                    Si no se envía ningún filtro, se devuelven todas las métricas paginadas.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de métricas de concentración.",
                    content = @Content(schema = @Schema(implementation = ConcentrationPaginatedResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos (por ejemplo, un 'period' fuera de los valores permitidos "
                            + "o una fecha con formato distinto de YYYY-MM-DD)."
            )
    })
    ResponseEntity<ConcentrationPaginatedResource> getConcentration(
            @Parameter(
                    description = "Cluster geográfico de movilidad (coincidencia exacta). Opcional. "
                            + "Ejemplos: CBD_BEIRAMAR, CENTRO_HISTORICO, TRINDADE, UFSC, CAMPECHE.",
                    example = "CBD_BEIRAMAR"
            )
            @RequestParam(required = false) String cluster,

            @Parameter(
                    description = "Fecha inicial del rango, inclusiva. Formato ISO-8601 (YYYY-MM-DD). Opcional.",
                    example = "2024-03-01"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,

            @Parameter(
                    description = "Fecha final del rango, inclusiva. Formato ISO-8601 (YYYY-MM-DD). Opcional.",
                    example = "2024-03-15"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,

            @Parameter(
                    description = "Período del día (franja horaria). Opcional, case-insensitive. "
                            + "Valores permitidos: DAWN (00–06h), MORNING (06–12h), AFTERNOON (12–18h), NIGHT (18–00h).",
                    example = "MORNING"
            )
            @RequestParam(required = false) String period,

            @Parameter(description = "Número de página, indexado desde 0 (0-based). Mínimo 0. Por defecto 0.", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Cantidad de elementos por página. Mínimo 1, máximo 100. Por defecto 20.", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    );
}
