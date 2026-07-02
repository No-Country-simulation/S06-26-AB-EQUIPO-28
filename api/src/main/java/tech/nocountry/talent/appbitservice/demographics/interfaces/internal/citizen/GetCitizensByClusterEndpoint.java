package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories.CitizenProfileRepository;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.resources.ClusterCitizenCountResource;

import java.util.List;

/**
 * Internal endpoint for retrieving citizen counts grouped by home cluster.
 *
 * <p>Used by other Bounded Contexts (e.g., inclusion-core) to obtain
 * demographic distribution data for social inclusion analysis.</p>
 */
@Component
@RequiredArgsConstructor
public class GetCitizensByClusterEndpoint {

    private final CitizenProfileRepository repository;

    /**
     * Retrieves citizen counts grouped by home cluster, with optional income level filter.
     *
     * @param incomeLevel optional income level filter (e.g., "D" for vulnerable). Null returns all.
     * @return list of cluster count resources
     */
    public List<ClusterCitizenCountResource> handle(String incomeLevel) {
        return repository.countByIncomeLevelGroupByCluster(incomeLevel)
                .stream()
                .map(proj -> new ClusterCitizenCountResource(proj.getClusterName(), proj.getCitizenCount()))
                .toList();
    }

    /**
     * Retrieves citizen counts grouped by home cluster, filtered by mobility pattern.
     *
     * @param pattern the mobility pattern (e.g., "LOW" for social isolation proxy)
     * @return list of cluster count resources
     */
    public List<ClusterCitizenCountResource> handleByMobilityPattern(String pattern) {
        return repository.countByMobilityPatternGroupByCluster(pattern)
                .stream()
                .map(proj -> new ClusterCitizenCountResource(proj.getClusterName(), proj.getCitizenCount()))
                .toList();
    }

    /**
     * Retrieves citizen counts grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the age groups to include (e.g., "18-24", "55+")
     * @return list of cluster count resources
     */
    public List<ClusterCitizenCountResource> handleByAgeGroups(List<String> ageGroups) {
        return repository.countByAgeGroupInGroupByCluster(ageGroups)
                .stream()
                .map(proj -> new ClusterCitizenCountResource(proj.getClusterName(), proj.getCitizenCount()))
                .toList();
    }
}
