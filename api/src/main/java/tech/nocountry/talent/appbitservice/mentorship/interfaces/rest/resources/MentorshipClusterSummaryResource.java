package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources.MentorshipClusterResourceDocs;

import java.util.List;
import java.util.Set;

/**
 * REST resource summarizing mentorship coverage in a geographic cluster.
 */
@Schema(description = MentorshipClusterResourceDocs.DESCRIPTION)
public record MentorshipClusterSummaryResource(
        @Schema(description = MentorshipClusterResourceDocs.CLUSTER_NAME)
        String clusterName,

        @Schema(description = MentorshipClusterResourceDocs.TOTAL_PROGRAMS)
        int totalPrograms,

        @Schema(description = MentorshipClusterResourceDocs.ACTIVE_PROGRAMS)
        int activePrograms,

        @Schema(description = MentorshipClusterResourceDocs.FOCUS_AREAS)
        Set<String> focusAreas,

        @Schema(description = MentorshipClusterResourceDocs.MODALITIES)
        Set<String> modalities,

        @Schema(description = MentorshipClusterResourceDocs.TARGET_AUDIENCES)
        Set<String> targetAudiences,

        @Schema(description = MentorshipClusterResourceDocs.TOTAL_CAPACITY)
        int totalCapacity,

        @Schema(description = MentorshipClusterResourceDocs.TOTAL_ACTIVE_MENTEES)
        int totalActiveMentees,

        @Schema(description = MentorshipClusterResourceDocs.PROGRAMS)
        List<MentorshipProgramSummaryResource> programs
) {
}