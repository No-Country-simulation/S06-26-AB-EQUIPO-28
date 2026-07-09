package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

import java.util.List;

/**
 * Domain result record produced by {@code GetMentorshipGapsUseCase}.
 *
 * <p>Represents the gap analysis for a single geographic cluster:
 * vulnerability metrics vs mentorship program coverage. This is a pure domain
 * record — resources (DTOs) are assembled from this in the REST transform layer.</p>
 *
 * <p>Stored under {@code domain/model/queries/} following the CQRS pattern where
 * query results are owned by the domain model, not by the interfaces layer.</p>
 *
 * @param clusterName            name of the geographic cluster
 * @param vulnerabilityScore     vulnerability index (0-100)
 * @param vulnerablePopulationCount number of vulnerable citizens
 * @param connectivityLevel      connectivity level string (HIGH, MEDIUM, LOW)
 * @param hasMentorshipPrograms  whether the cluster has any active mentorship programs
 * @param matchingProgramIds     list of matching program IDs in the cluster
 * @param gapSeverity            gap severity: CRITICAL, HIGH, MODERATE
 */
public record MentorshipGapResult(
        String clusterName,
        int vulnerabilityScore,
        int vulnerablePopulationCount,
        String connectivityLevel,
        boolean hasMentorshipPrograms,
        List<String> matchingProgramIds,
        String gapSeverity
) {
    /** Severity constant: no programs in a priority intervention cluster. */
    public static final String CRITICAL = "CRITICAL";
    /** Severity constant: no programs and score above threshold. */
    public static final String HIGH = "HIGH";
    /** Severity constant: moderate or no gap. */
    public static final String MODERATE = "MODERATE";
}