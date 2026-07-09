package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources.MentorshipProgramResourceDocs;

import java.time.Instant;
import java.time.LocalDate;

/**
 * REST resource for mentorship programs.
 *
 * <p>This DTO represents a full mentorship program entity
 * in API responses. Uses {@code @Schema} annotations sourced from
 * {@link MentorshipProgramResourceDocs} for reusable OpenAPI descriptions.</p>
 */
@Schema(description = MentorshipProgramResourceDocs.DESCRIPTION)
public record MentorshipProgramResource(
        @Schema(description = MentorshipProgramResourceDocs.PROGRAM_ID)
        String programId,

        @Schema(description = MentorshipProgramResourceDocs.NAME)
        String name,

        @Schema(description = MentorshipProgramResourceDocs.DESCRIPTION_FIELD)
        String description,

        @Schema(description = MentorshipProgramResourceDocs.ORGANIZATION)
        String organization,

        @Schema(description = MentorshipProgramResourceDocs.FOCUS_AREA)
        String focusArea,

        @Schema(description = MentorshipProgramResourceDocs.MODALITY)
        String modality,

        @Schema(description = MentorshipProgramResourceDocs.TARGET_AUDIENCE)
        String targetAudience,

        @Schema(description = MentorshipProgramResourceDocs.TARGET_INCOME_LEVEL)
        String targetIncomeLevel,

        @Schema(description = MentorshipProgramResourceDocs.CLUSTER_NAME)
        String clusterName,

        @Schema(description = MentorshipProgramResourceDocs.TOTAL_CAPACITY)
        int totalCapacity,

        @Schema(description = MentorshipProgramResourceDocs.ACTIVE_MENTEES)
        int activeMentees,

        @Schema(description = MentorshipProgramResourceDocs.START_DATE)
        LocalDate startDate,

        @Schema(description = MentorshipProgramResourceDocs.END_DATE)
        LocalDate endDate,

        @Schema(description = MentorshipProgramResourceDocs.IS_ACTIVE)
        boolean isActive,

        @Schema(description = MentorshipProgramResourceDocs.WEBSITE_URL)
        String websiteUrl,

        @Schema(description = MentorshipProgramResourceDocs.CONTACT_EMAIL)
        String contactEmail,

        @Schema(description = MentorshipProgramResourceDocs.CREATED_AT)
        Instant createdAt,

        @Schema(description = MentorshipProgramResourceDocs.UPDATED_AT)
        Instant updatedAt
) {
}