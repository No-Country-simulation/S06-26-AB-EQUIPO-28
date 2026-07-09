package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources.MentorshipProgramResourceDocs;

/**
 * Lightweight summary of a mentorship program, used when listing
 * programs inside a cluster or gap analysis result.
 */
@Schema(description = "Resumen de un programa de mentoría")
public record MentorshipProgramSummaryResource(
        @Schema(description = MentorshipProgramResourceDocs.PROGRAM_ID)
        String programId,

        @Schema(description = MentorshipProgramResourceDocs.NAME)
        String name,

        @Schema(description = MentorshipProgramResourceDocs.FOCUS_AREA)
        String focusArea,

        @Schema(description = MentorshipProgramResourceDocs.MODALITY)
        String modality,

        @Schema(description = MentorshipProgramResourceDocs.IS_ACTIVE)
        boolean isActive
) {
}