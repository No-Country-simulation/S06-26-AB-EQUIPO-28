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
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimePaginatedResource;

/**
 * OpenAPI contract para los endpoints de tiempos de viaje inter-cluster.
 */
@Tag(name = "Employability - Travel Times", description = "API para consulta de tiempos de viaje inter-cluster del dataset CDRView")
public interface TravelTimeDocs {

    @Operation(
            summary = "Listar tiempos de viaje inter-cluster",
            description = """
                    Recupera una lista paginada y filtrada de registros de tiempos de viaje \
                    inter-cluster del dataset CDRView. Cada registro agrega las observaciones de \
                    desplazamiento entre dos clusters: número de observaciones, distancia \
                    promedio y percentiles 25/75, junto con el período de sesión predominante."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de tiempos de viaje paginada",
                    content = @Content(schema = @Schema(implementation = TravelTimePaginatedResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de paginación o filtro inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<TravelTimePaginatedResource> getTravelTimes(
            @Parameter(description = "Número de página (empieza en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página (1-100)", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Criterio de ordenamiento: campo,dirección. Ejemplo: createdAt,desc", example = "createdAt,desc")
            @RequestParam(defaultValue = "createdAt,desc") String sort,

            @Parameter(description = "Filtrar por cluster de origen. Opcional.")
            @RequestParam(required = false) String originCluster,

            @Parameter(description = "Filtrar por cluster de destino. Opcional.")
            @RequestParam(required = false) String destinationCluster
    );
}
