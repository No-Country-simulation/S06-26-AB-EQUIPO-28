package tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources.MobilityODPairPaginatedResourceDocs;

import java.util.List;

/**
 * Paginated resource for the list of mobility OD pairs.
 *
 * <p>Wraps a list of {@link MobilityODPairResource} with pagination metadata,
 * siguiendo el mismo contrato que {@code MentorshipProgramPaginatedResource}.</p>
 *
 * @param content       lista de pares OD en la página actual
 * @param totalElements total de elementos que coinciden con la query (todas las páginas)
 * @param currentPage   número de página actual (0-based)
 * @param pageSize      cantidad de elementos por página
 * @param totalPages    cantidad total de páginas disponibles
 */
@Schema(description = MobilityODPairPaginatedResourceDocs.DESCRIPTION)
public record MobilityODPairPaginatedResource(
        @Schema(description = MobilityODPairPaginatedResourceDocs.CONTENT)
        List<MobilityODPairResource> content,

        @Schema(description = MobilityODPairPaginatedResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = MobilityODPairPaginatedResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = MobilityODPairPaginatedResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = MobilityODPairPaginatedResourceDocs.TOTAL_PAGES)
        int totalPages
) {}
