package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore;

/**
 * ACL result record in mentorship BC language.
 *
 * <p>Represents a vulnerable geographic region translated from the upstream
 * {@code inclusioncore.interfaces.rest.resources.VulnerableRegionResource}.
 * The field {@code clusterName} is semantically equivalent to the upstream's
 * {@code regionName} — in mentorship, the domain refers to geographic
 * groups as "clusters" (matching {@code MentorshipProgram.clusterName}).</p>
 *
 * <p>This record is the output of {@link InclusionCoreAclFacade} and is consumed
 * by mentorship query use cases (e.g., {@code GetMentorshipGapsQueryUseCase})
 * to compute gap analysis.</p>
 */
public record VulnerableClusterAclResult(
        String clusterName,
        int vulnerabilityScore,
        String vulnerabilityLevel,
        int vulnerablePopulationCount,
        int totalPopulation,
        double vulnerablePercentage,
        String connectivityLevel,
        double concentrationIndex,
        boolean isPriorityForIntervention
) {
}