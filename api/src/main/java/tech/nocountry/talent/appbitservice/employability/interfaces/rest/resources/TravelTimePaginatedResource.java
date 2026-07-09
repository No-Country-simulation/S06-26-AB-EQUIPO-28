package tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources.TravelTimePaginatedResourceDocs;

import java.util.List;

/**
 * Paginated resource for the list of inter-cluster travel times.
 *
 * <p>Wraps a list of {@link TravelTimeResource} with pagination metadata,
 * siguiendo el mismo contrato que {@code MentorshipProgramPaginatedResource}.</p>
 *
 * @param content       lista de registros de tiempos de viaje en la página actual
 * @param totalElements total de elementos que coinciden con la query (todas las páginas)
 * @param currentPage   número de página actual (0-based)
 * @param pageSize      cantidad de elementos por página
 * @param totalPages    cantidad total de páginas disponibles
 */
@Schema(description = TravelTimePaginatedResourceDocs.DESCRIPTION)
public record TravelTimePaginatedResource(
        @Schema(description = TravelTimePaginatedResourceDocs.CONTENT)
        List<TravelTimeResource> content,

        @Schema(description = TravelTimePaginatedResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = TravelTimePaginatedResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = TravelTimePaginatedResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = TravelTimePaginatedResourceDocs.TOTAL_PAGES)
        int totalPages
) {}
