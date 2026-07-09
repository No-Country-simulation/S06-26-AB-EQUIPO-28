package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources.MentorshipGapResourceDocs;

import java.util.List;

/**
 * REST resource for mentorship gap analysis results.
 *
 * <p>Represents a geographic cluster where a vulnerability gap exists:
 * high social vulnerability but insufficient mentorship program coverage.</p>
 */
@Schema(description = MentorshipGapResourceDocs.DESCRIPTION)
public record MentorshipGapResource(
        @Schema(description = MentorshipGapResourceDocs.CLUSTER_NAME)
        String clusterName,

        @Schema(description = MentorshipGapResourceDocs.VULNERABILITY_SCORE)
        int vulnerabilityScore,

        @Schema(description = MentorshipGapResourceDocs.VULNERABLE_POPULATION_COUNT)
        int vulnerablePopulationCount,

        @Schema(description = MentorshipGapResourceDocs.CONNECTIVITY_LEVEL)
        String connectivityLevel,

        @Schema(description = MentorshipGapResourceDocs.HAS_MENTORSHIP_PROGRAMS)
        boolean hasMentorshipPrograms,

        @Schema(description = MentorshipGapResourceDocs.MATCHING_PROGRAMS)
        List<MentorshipProgramSummaryResource> matchingPrograms,

        @Schema(description = MentorshipGapResourceDocs.GAP_SEVERITY)
        String gapSeverity
) {
}