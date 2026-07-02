package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources.CitizenPagingDocs;

import java.util.List;

/**
 * Recurso paginado para la lista de ciudadanos.
 *
 * <p>Envuelve una lista de {@link CitizenResource} con metadatos de paginación,
 * permitiendo al cliente navegar por grandes conjuntos de datos de forma eficiente.</p>
 *
 * @param content       lista de ciudadanos de la página actual
 * @param totalElements total de elementos que coinciden con la consulta
 * @param currentPage   número de página actual (0-based)
 * @param pageSize      cantidad de elementos por página
 * @param totalPages    total de páginas disponibles
 */
@Schema(description = """
    Respuesta paginada con la lista de ciudadanos.

    La paginación permite obtener grandes cantidades de datos
    de forma eficiente, dividiéndolos en páginas.
    """)
public record CitizenPaginatedResource(
        @Schema(description = CitizenPagingDocs.CONTENT)
        List<CitizenResource> content,

        @Schema(description = CitizenPagingDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = CitizenPagingDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = CitizenPagingDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = CitizenPagingDocs.TOTAL_PAGES)
        int totalPages
) {}
