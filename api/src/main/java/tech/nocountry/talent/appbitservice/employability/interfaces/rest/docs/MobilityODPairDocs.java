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
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairPaginatedResource;

/**
 * OpenAPI contract para los endpoints de la matriz OD de movilidad.
 */
@Tag(name = "Employability - OD Matrix", description = "API para consulta de la matriz origen-destino de movilidad del dataset CDRView")
public interface MobilityODPairDocs {

    @Operation(
            summary = "Listar pares origen-destino de movilidad",
            description = """
                    Recupera una lista paginada y filtrada de pares OD de movilidad del dataset \
                    CDRView. Cada registro describe el flujo observado entre dos clusters \
                    geográficos: usuarios únicos, viajes totales, distancia promedio (Haversine) \
                    y período de sesión predominante."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pares OD paginada",
                    content = @Content(schema = @Schema(implementation = MobilityODPairPaginatedResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de paginación o filtro inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    ResponseEntity<MobilityODPairPaginatedResource> getODPairs(
            @Parameter(description = "Número de página (empieza en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página (1-100)", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Criterio de ordenamiento: campo,dirección. Ejemplo: createdAt,desc", example = "createdAt,desc")
            @RequestParam(defaultValue = "createdAt,desc") String sort,

            @Parameter(description = "Filtrar por cluster de origen. Opcional.")
            @RequestParam(required = false) String originCluster,

            @Parameter(description = "Filtrar por cluster de destino. Opcional.")
            @RequestParam(required = false) String destinationCluster,

            @Parameter(description = "Filtrar por período predominante: DAWN, MORNING, AFTERNOON, NIGHT. Opcional.")
            @RequestParam(required = false) String predominantPeriod
    );
}
