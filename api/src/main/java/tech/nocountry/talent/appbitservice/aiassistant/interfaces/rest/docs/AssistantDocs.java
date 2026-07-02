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
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantQueryResource;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantResponseResource;

/**
 * OpenAPI contract for the AI Assistant REST API.
 */
@Tag(name = "AI Assistant", description = "API de asistencia con IA para gestores gubernamentales")
public interface AssistantDocs {

    @Operation(
            summary = "Consultar datos de inclusión social en lenguaje natural",
            description = """
                    Permite a los gestores públicos realizar preguntas en lenguaje natural sobre
                    indicadores de inclusión social, empleo, salud mental y brechas de infraestructura.

                    Flujo interno:
                    1. Analiza las palabras de la pregunta para determinar qué datos cargar
                       (regiones vulnerables, salud mental y/o empleabilidad). Si no se detecta
                       ningún tema, carga todos los indicadores como contexto general.
                    2. Consulta esos indicadores cruzando demografía y telemetría de red.
                    3. Construye un prompt incluyendo los datos como contexto.
                    4. Envía el prompt al modelo de lenguaje (OpenRouter) y retorna un análisis
                       ejecutivo con recomendaciones, las fuentes usadas y metadatos de generación.

                    Cuerpo de la petición (AssistantQueryResource):
                    - question (obligatorio): pregunta en lenguaje natural.
                    - context (opcional): lista de parámetros que acotan la consulta.
                    - history (opcional): historial de la conversación para preguntas de seguimiento.

                    Límite de uso: máximo 10 peticiones por minuto por cliente (IP o cabecera
                    X-API-Key). Al superarlo se devuelve 429 Too Many Requests.

                    Ejemplos de preguntas:
                    - "¿Cuáles son las regiones con mayor vulnerabilidad digital?"
                    - "¿Dónde hay brechas de infraestructura que impiden telemedicina?"
                    - "¿Qué programas de empleo podemos implementar en zonas de baja conectividad?"
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuesta generada exitosamente",
                    content = @Content(schema = @Schema(implementation = AssistantResponseResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (p. ej. 'question' vacío o 'role' del historial distinto de 'user'/'assistant')",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too Many Requests - Límite de 10 peticiones por minuto excedido",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor (p. ej. datos de inclusión no disponibles o fallo de comunicación con el LLM)",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    ResponseEntity<AssistantResponseResource> processQuery(
            @Valid @RequestBody AssistantQueryResource queryResource
    );
}