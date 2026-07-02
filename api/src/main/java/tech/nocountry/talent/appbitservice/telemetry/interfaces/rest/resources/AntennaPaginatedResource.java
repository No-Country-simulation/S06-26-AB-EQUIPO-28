package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources.AntennaPaginatedResourceDocs;

import java.util.List;

/**
 * Recurso paginado para la lista de antenas de telecomunicación.
 *
 * <p>Envuelve una lista de {@link AntennaResource} con metadatos de paginación,
 * permitiendo al cliente navegar por grandes conjuntos de datos de forma eficiente.</p>
 *
 * @param content       lista de antenas de la página actual
 * @param totalElements total de elementos que coinciden con la consulta
 * @param currentPage   número de página actual (0-based)
 * @param pageSize      cantidad de elementos por página
 * @param totalPages    total de páginas disponibles
 */
@Schema(description = AntennaPaginatedResourceDocs.DESCRIPTION)
public record AntennaPaginatedResource(
        @Schema(description = AntennaPaginatedResourceDocs.CONTENT)
        List<AntennaResource> content,

        @Schema(description = AntennaPaginatedResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = AntennaPaginatedResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = AntennaPaginatedResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = AntennaPaginatedResourceDocs.TOTAL_PAGES)
        int totalPages
) {}
