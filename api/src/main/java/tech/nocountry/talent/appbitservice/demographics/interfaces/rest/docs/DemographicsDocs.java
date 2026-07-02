package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenPaginatedResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CreateCitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.UpdateCitizenResource;

/**
 * Interfaz de documentación OpenAPI para el contexto de demografía.
 *
 * <p>Contiene las anotaciones de OpenAPI en español para documentar los endpoints
 * del bounded context de demographics.</p>
 */
@Tag(
        name = "Demographics",
        description = "API para la gestión y consulta de perfiles demográficos de ciudadanos/abonados "
                + "(nivel de ingreso, grupo etario, patrón de movilidad y cluster de residencia). "
                + "Estos datos se cruzan con la telemetría de red para identificar regiones vulnerables "
                + "y orientar políticas de inclusión social.")
public interface DemographicsDocs {

    // -----------------------------
    // Query Endpoints — unified
    // -----------------------------

    /**
     * Obtiene ciudadanos con filtros opcionales y paginación obligatoria.
     * <p>
     * Permite filtrar por nivel de ingreso ({@code incomeLevel}) y/o grupo de edad
     * ({@code ageGroup}). Siempre retorna una respuesta paginada
     * ({@link CitizenPaginatedResource}).</p>
     */
    @Operation(
            summary = "Listar ciudadanos con filtros y paginación",
            description = "Retorna los perfiles demográficos de ciudadanos en formato paginado. "
                    + "Permite filtrar de forma opcional por nivel de ingreso (incomeLevel: A, B, C, D) "
                    + "y/o por grupo etario (ageGroup: 18-24, 25-34, 35-44, 45-54, 55+). "
                    + "Si no se envían filtros, devuelve todos los ciudadanos paginados. "
                    + "La respuesta siempre incluye metadatos de paginación "
                    + "(totalElements, currentPage, pageSize, totalPages)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de ciudadanos obtenida exitosamente. "
                            + "El contenido (content) puede estar vacío si ningún ciudadano coincide con los filtros.",
                    content = @Content(schema = @Schema(implementation = CitizenPaginatedResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos: 'page' debe ser >= 0 y 'size' debe estar entre 1 y 100."
            )
    })
    ResponseEntity<CitizenPaginatedResource> getAllCitizens(
            @Parameter(
                    description = "Filtro opcional por nivel de ingreso (faja de renta). "
                            + "Valores permitidos: A (alta), B (media-alta), C (media), D (baja/vulnerable). "
                            + "Si se omite, no se filtra por ingreso.",
                    example = "D")
            @RequestParam(required = false) String incomeLevel,
            @Parameter(
                    description = "Filtro opcional por grupo etario. "
                            + "Valores permitidos: 18-24, 25-34, 35-44, 45-54, 55+. "
                            + "Si se omite, no se filtra por edad.",
                    example = "18-24")
            @RequestParam(required = false) String ageGroup,
            @Parameter(
                    description = "Número de página solicitada, comenzando en 0 (0-based). "
                            + "Debe ser >= 0. Valor por defecto: 0.",
                    example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(
                    description = "Cantidad de elementos por página. "
                            + "Rango permitido: de 1 a 100. Valor por defecto: 20.",
                    example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    );

    // -------------------------------------------------------------------
    // Command Endpoints
    // -------------------------------------------------------------------

    /**
     * Crea un nuevo perfil de ciudadano.
     */
    @Operation(
            summary = "Crear nuevo ciudadano",
            description = "Crea un nuevo perfil demográfico de ciudadano. Todos los campos son obligatorios. "
                    + "El citizenHash debe ser único y tener entre 32 y 64 caracteres; incomeLevel debe ser "
                    + "A, B, C o D; ageGroup uno de 18-24, 25-34, 35-44, 45-54, 55+; mobilityPattern uno de "
                    + "LOW, MODERATE, INTENSE; homeCluster un texto de máximo 50 caracteres. "
                    + "Devuelve el recurso creado y la cabecera Location con su URL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ciudadano creado exitosamente. La cabecera Location apunta al recurso creado.",
                    content = @Content(schema = @Schema(implementation = CitizenResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos: falta un campo obligatorio o un valor "
                            + "no respeta los valores/longitudes permitidos."
            )
    })
    ResponseEntity<CitizenResource> createCitizen(
            @Valid @RequestBody CreateCitizenResource resource
    );

    /**
     * Actualiza un perfil de ciudadano existente.
     */
    @Operation(
            summary = "Actualizar ciudadano",
            description = "Actualiza parcialmente un perfil demográfico existente, identificado por su hash en la ruta. "
                    + "Solo se aplican los campos no nulos enviados en el cuerpo (incomeLevel, ageGroup, "
                    + "mobilityPattern, homeCluster); los campos omitidos conservan su valor actual. "
                    + "Cada valor enviado debe respetar los valores permitidos de su faja. El hash no se modifica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ciudadano actualizado exitosamente. Devuelve el perfil con los valores resultantes.",
                    content = @Content(schema = @Schema(implementation = CitizenResource.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos: algún valor enviado no respeta los valores/longitudes permitidos."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un ciudadano con el hash indicado."
            )
    })
    ResponseEntity<CitizenResource> updateCitizen(
            @Parameter(
                    description = "Hash anonimizado del ciudadano a actualizar (identificador único).",
                    example = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
                    required = true)
            @PathVariable String citizenId,
            @Valid @RequestBody UpdateCitizenResource resource
    );

    /**
     * Elimina un perfil de ciudadano.
     */
    @Operation(
            summary = "Eliminar ciudadano",
            description = "Elimina de forma permanente el perfil demográfico de un ciudadano, "
                    + "identificado por su hash anonimizado en la ruta. Operación idempotente desde la "
                    + "perspectiva del recurso: si el ciudadano no existe, retorna 404."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Ciudadano eliminado exitosamente. Sin contenido en la respuesta."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un ciudadano con el hash indicado."
            )
    })
    ResponseEntity<Void> deleteCitizen(
            @Parameter(
                    description = "Hash anonimizado del ciudadano a eliminar (identificador único).",
                    example = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
                    required = true)
            @PathVariable String citizenId
    );
}