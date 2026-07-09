package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources.MentorshipProgramPaginatedResourceDocs;

import java.util.List;

/**
 * Paginated resource for the list of mentorship programs.
 *
 * <p>Wraps a list of {@link MentorshipProgramResource} with pagination metadata,
 * allowing the client to navigate large datasets efficiently. Follows the same
 * pagination contract used by {@code AntennaPaginatedResource} and
 * {@code CitizenPaginatedResource}.</p>
 *
 * @param content       list of programs in the current page
 * @param totalElements total elements matching the query (across all pages)
 * @param currentPage   current page number (0-based)
 * @param pageSize      number of elements per page
 * @param totalPages    total number of available pages
 */
@Schema(description = MentorshipProgramPaginatedResourceDocs.DESCRIPTION)
public record MentorshipProgramPaginatedResource(
        @Schema(description = MentorshipProgramPaginatedResourceDocs.CONTENT)
        List<MentorshipProgramResource> content,

        @Schema(description = MentorshipProgramPaginatedResourceDocs.TOTAL_ELEMENTS)
        long totalElements,

        @Schema(description = MentorshipProgramPaginatedResourceDocs.CURRENT_PAGE)
        int currentPage,

        @Schema(description = MentorshipProgramPaginatedResourceDocs.PAGE_SIZE)
        int pageSize,

        @Schema(description = MentorshipProgramPaginatedResourceDocs.TOTAL_PAGES)
        int totalPages
) {}
