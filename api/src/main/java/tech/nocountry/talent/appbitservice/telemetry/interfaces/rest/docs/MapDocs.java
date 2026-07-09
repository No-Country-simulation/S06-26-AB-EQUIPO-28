package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Interfaz de documentación OpenAPI para el contexto de mapa.
 *
 * <p>Contiene las anotaciones de OpenAPI en español para documentar los endpoints
 * del recurso de visualización geográfica de regiones.</p>
 */
@Tag(name = "Map", description = "API de visualización geográfica de regiones. "
        + "Agrupa datos de antenas y concentración de red por cluster/region, "
        + "mostrando métricas de conectividad, usuarios promedio y calidad de red "
        + "para el mapa interactivo del dashboard B2G.")
public interface MapDocs {

    /**
     * Retorna todas las regiones con métricas agregadas de antenas y concentración.
     *
     * <p>Cada región incluye:
     * <ul>
     *   <li>name: nombre del cluster geográfico</li>
     *   <li>lat/lng: coordenadas del centroide (promedio de antenas del cluster)</li>
     *   <li>concentration: cantidad total de registros de concentración</li>
     *   <li>connectivity: porcentaje de cobertura de red (100 − avgDropPct)</li>
     *   <li>indicators: objeto con antennas, averageUsers, averageDropPct, averageCongestion</li>
     * </ul>
     */
    @Operation(
            summary = "Obtener regiones con métricas agregadas",
            description = """
                    Retorna todas las regiones (clusters) del sistema con métricas agregadas de
                    antenas y concentración de red, diseñadas para su visualización en un mapa interactivo.

                    Flujo interno:
                    1. Carga todas las antenas del dataset de telemetría.
                    2. Carga todas las métricas de concentración de red.
                    3. Cruza antenas y concentración por ECGI → cluster.
                    4. Calcula el centroide (promedio de lat/lng) de las antenas de cada cluster.
                    5. Agrega métricas: usuarios promedio, drop promedio, congestión promedio.
                    6. Calcula conectividad = 100 − avgDropPct (mayor drop = peor cobertura).

                    La respuesta es un objeto con un array 'regions', donde cada elemento representa
                    un cluster geográfico con sus coordenadas y métricas de inclusión digital.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de regiones con métricas agregadas obtenida exitosamente."
            )
    })
    ResponseEntity<Map<String, Object>> getRegions();
}
