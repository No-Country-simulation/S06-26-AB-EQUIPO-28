package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics;

import java.util.List;

/**
 * ACL Port for the Demographics bounded context — employability side.
 *
 * <p>This interface is consumed by the employability use cases to obtain
 * demographic distribution data (citizen counts grouped by home cluster) used
 * as the demand side of the employability-gap analysis. It abstracts the origin
 * of the data using only the employability bounded context's ubiquitous
 * language: {@link EmployabilityClusterCitizenSummary}.</p>
 *
 * <p>The employability BC depends ONLY on this interface, never directly on the
 * demographics model. Implementation: {@link EmployabilityDemographicsAclFacade}.</p>
 */
public interface EmployabilityDemographicsAclPort {

    /**
     * Gets citizen counts grouped by home cluster, with no filter.
     *
     * @return list of cluster citizen summaries
     */
    List<EmployabilityClusterCitizenSummary> getCitizenCountByCluster();

    /**
     * Gets citizen counts grouped by home cluster, filtered by income level.
     *
     * @param incomeLevel optional income level filter (e.g., "D" for vulnerable). Null returns all clusters.
     * @return list of cluster citizen summaries
     */
    List<EmployabilityClusterCitizenSummary> getCitizenCountByClusterAndIncomeLevel(String incomeLevel);

    /**
     * Gets citizen counts grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the age groups to include (e.g., "18-24", "55+")
     * @return list of cluster citizen summaries
     */
    List<EmployabilityClusterCitizenSummary> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups);
}