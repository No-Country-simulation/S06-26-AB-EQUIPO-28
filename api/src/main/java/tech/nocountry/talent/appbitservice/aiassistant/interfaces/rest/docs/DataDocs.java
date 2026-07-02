package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.controllers.DataController.DataRequest;

import java.util.Map;

/**
 * Interfaz de documentación OpenAPI para el contexto de datos del challenge.
 *
 * <p>Contiene las anotaciones de OpenAPI en español para documentar el endpoint
 * de consulta en lenguaje natural en el formato esperado por el challenge B2G.</p>
 */
@Tag(name = "Data", description = "API de consulta de datos en formato challenge B2G. "
        + "Acepta preguntas en lenguaje natural con filtros de región e indicador, "
        + "y retorna respuestas estructuradas con datos, fuentes y análisis del agente IA.")
public interface DataDocs {

    /**
     * Procesa una consulta en lenguaje natural y retorna datos estructurados.
     *
     * <p>Cuerpo de la petición (DataRequest):
     * <ul>
     *   <li>query (obligatorio): pregunta en lenguaje natural</li>
     *   <li>filters (opcional): objeto con region e indicator</li>
     *   <li>language (opcional): idioma de la consulta</li>
     * </ul>
     *
     * <p>Formato de respuesta:
     * <ul>
     *   <li>aiResponse: análisis ejecutivo generado por el agente IA</li>
     *   <li>data: array de objetos con region, value y source</li>
     *   <li>sources: fuentes de datos utilizadas</li>
     *   <li>timestamp: marca de tiempo ISO-8601 de la respuesta</li>
     * </ul>
     */
    @Operation(
            summary = "Consultar datos en formato challenge",
            description = """
                    Procesa una consulta en lenguaje natural y retorna datos estructurados
                    en el formato esperado por el challenge B2G.

                    Flujo interno:
                    1. Mapea los filtros (region, indicator) al contexto del asistente IA existente.
                    2. Procesa la consulta a través del pipeline de IA (mismo que el endpoint de asistente).
                    3. Transforma la respuesta al formato challenge: aiResponse, data[] y sources[].

                    Ejemplos de uso:
                    - { "query": "¿Cuáles son las regiones más vulnerables?" }
                    - { "query": "Brechas de salud mental", "filters": { "region": "Norte", "indicator": "salud_mental" } }
                    - { "query": "Empleabilidad en zonas de baja conectividad", "language": "es" }

                    Límite de uso: máximo 10 peticiones por minuto por cliente (IP o cabecera
                    X-API-Key). Al superarlo se devuelve 429 Too Many Requests.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuesta generada exitosamente con datos estructurados."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (p. ej. 'query' vacío o campos con tipos incorrectos).",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too Many Requests - Límite de 10 peticiones por minuto excedido.",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor (p. ej. datos de inclusión no disponibles o fallo de comunicación con el LLM).",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    ResponseEntity<Map<String, Object>> processQuery(
            @Valid @RequestBody DataRequest request
    );
}
