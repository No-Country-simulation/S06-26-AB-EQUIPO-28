package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

import java.util.List;
import java.util.Set;

/**
 * Domain result record produced by {@code GetMentorshipClustersUseCase}.
 *
 * <p>Aggregates mentorship program coverage for a single geographic cluster.
 * A pure domain record — resources (DTOs) are assembled from this in the
 * REST transform layer.</p>
 *
 * @param clusterName       name of the geographic cluster
 * @param totalPrograms     total number of programs in the cluster
 * @param activePrograms    number of currently active programs
 * @param focusAreas        distinct focus areas represented
 * @param modalities        distinct delivery modalities
 * @param targetAudiences   distinct target audiences
 * @param totalCapacity     sum of capacity across programs
 * @param totalActiveMentees sum of enrolled mentees
 * @param programIds        list of program IDs in the cluster
 */
public record MentorshipClusterSummary(
        String clusterName,
        int totalPrograms,
        int activePrograms,
        Set<String> focusAreas,
        Set<String> modalities,
        Set<String> targetAudiences,
        int totalCapacity,
        int totalActiveMentees,
        List<String> programIds
) {
}