package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.resources.UserResourceDocs;

import java.util.List;

@Schema(description = "Recurso de respuesta paginada para usuarios")
public record UserPaginatedResource(
        @Schema(description = UserResourceDocs.CONTENT)
        List<UserResource> content,

        @Schema(description = UserResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = UserResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = UserResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = UserResourceDocs.TOTAL_PAGES)
        int totalPages
) { }