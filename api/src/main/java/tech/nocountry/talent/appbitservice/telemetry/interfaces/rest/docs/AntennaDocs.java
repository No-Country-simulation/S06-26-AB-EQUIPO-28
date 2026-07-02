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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;

/**
 * Interfaz de documentación OpenAPI para el contexto de antenas.
 *
 * <p>Contiene las anotaciones de OpenAPI en español para documentar los endpoints
 * del recurso de antenas de telecomunicación.</p>
 */
@Tag(name = "Antennas", description = "API de consulta de antenas (ERB / celdas) de la red móvil. "
        + "Datos del dataset de telemetría de la Región Metropolitana de Florianópolis (Brasil), "
        + "usados como base para análisis de inclusión digital.")
public interface AntennaDocs {

    /**
     * Obtiene todas las antenas del sistema con paginación.
     */
    @Operation(
            summary = "Listar todas las antenas",
            description = """
                    Retorna la lista paginada de antenas (ERB / celdas) registradas en el sistema.
                    Cada antena incluye su identificador ECGI, el cluster geográfico, el municipio y sus coordenadas WGS-84.
                    Use los parámetros 'page' y 'size' para navegar el conjunto de resultados.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de antenas obtenida exitosamente.",
                    content = @Content(schema = @Schema(implementation = AntennaPaginatedResource.class))
            )
    })
    ResponseEntity<AntennaPaginatedResource> getAllAntennas(
            @Parameter(description = "Número de página, indexado desde 0 (0-based). Mínimo 0. Por defecto 0.", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Cantidad de elementos por página. Mínimo 1, máximo 100. Por defecto 20.", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    );

    /**
     * Obtiene una antenna por su identificador ECGI.
     */
    @Operation(
            summary = "Obtener antena por ECGI",
            description = """
                    Retorna los datos de una antena específica identificada por su ECGI
                    (E-UTRAN Cell Global Identifier). El ECGI es una cadena de hasta 12 caracteres
                    y debe tratarse siempre como texto (puede contener ceros a la izquierda).
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Antena encontrada.",
                    content = @Content(schema = @Schema(implementation = AntennaResource.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe ninguna antena con el ECGI indicado."
            )
    })
    ResponseEntity<AntennaResource> getAntennaByEcgi(
            @Parameter(
                    description = "Identificador ECGI de la antena (cadena de hasta 12 caracteres; tratar siempre como texto).",
                    example = "530011001234"
            )
            @PathVariable String ecgi
    );
}
