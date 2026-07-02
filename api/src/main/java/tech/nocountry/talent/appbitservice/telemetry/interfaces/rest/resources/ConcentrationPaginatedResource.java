package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources.ConcentrationPaginatedResourceDocs;

import java.util.List;

/**
 * Recurso paginado para la lista de métricas de concentración de red.
 *
 * <p>Envuelve una lista de {@link ConcentrationResource} con metadatos de paginación,
 * permitiendo al cliente navegar por grandes conjuntos de datos de forma eficiente.</p>
 *
 * @param content       lista de concentraciones de la página actual
 * @param totalElements total de elementos que coinciden con la consulta
 * @param currentPage   número de página actual (0-based)
 * @param pageSize      cantidad de elementos por página
 * @param totalPages    total de páginas disponibles
 */
@Schema(description = ConcentrationPaginatedResourceDocs.DESCRIPTION)
public record ConcentrationPaginatedResource(
        @Schema(description = ConcentrationPaginatedResourceDocs.CONTENT)
        List<ConcentrationResource> content,

        @Schema(description = ConcentrationPaginatedResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = ConcentrationPaginatedResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = ConcentrationPaginatedResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = ConcentrationPaginatedResourceDocs.TOTAL_PAGES)
        int totalPages
) {}
