package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.GetCitizensByClusterEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.GetVulnerableCitizensEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.ClusterCountAclResult;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.resources.DemographicsCitizenResource;

import java.util.List;

/**
 * Adapter para consumir los endpoints internos del Bounded Context de Demografía.
 *
 * <p>Inyecta directamente los beans {@code @Component} de los endpoints internos
 * en lugar de hacer llamadas HTTP, ya que es un monolito modular.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DemographicsInternalClient {
    private final GetVulnerableCitizensEndpoint getVulnerableCitizensEndpoint;
    private final GetCitizensByClusterEndpoint getCitizensByClusterEndpoint;

    /**
     * Obtiene los ciudadanos vulnerables (nivel de ingreso D).
     *
     * @return lista de recursos de ciudadanos vulnerables
     */
    public List<DemographicsCitizenResource> getVulnerableCitizens() {
        log.debug("Fetching vulnerable citizens via direct invocation");
        var resources = getVulnerableCitizensEndpoint.handle();
        return resources.stream()
                .map(this::toDemographicsResource)
                .toList();
    }

    /**
     * Gets citizen counts grouped by home cluster with optional income level filter.
     *
     * @param incomeLevel optional income level filter (nullable)
     * @return list of cluster count ACL results
     */
    public List<ClusterCountAclResult> getCitizenCountByCluster(String incomeLevel) {
        log.debug("Fetching citizen count by cluster via direct invocation, incomeLevel={}", incomeLevel);
        return getCitizensByClusterEndpoint.handle(incomeLevel)
                .stream()
                .map(res -> new ClusterCountAclResult(res.clusterName(), res.citizenCount()))
                .toList();
    }

    /**
     * Gets citizen counts grouped by home cluster, filtered by mobility pattern.
     *
     * @param pattern the mobility pattern (e.g., "LOW" for social isolation proxy)
     * @return list of cluster count ACL results
     */
    public List<ClusterCountAclResult> getCitizenCountByClusterAndMobilityPattern(String pattern) {
        log.debug("Fetching citizen count by cluster and mobility pattern via direct invocation, pattern={}", pattern);
        return getCitizensByClusterEndpoint.handleByMobilityPattern(pattern)
                .stream()
                .map(res -> new ClusterCountAclResult(res.clusterName(), res.citizenCount()))
                .toList();
    }

    /**
     * Gets citizen counts grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the age groups to filter (e.g., "18-24", "55+")
     * @return list of cluster count ACL results
     */
    public List<ClusterCountAclResult> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups) {
        log.debug("Fetching citizen count by cluster and age groups via direct invocation, ageGroups={}", ageGroups);
        return getCitizensByClusterEndpoint.handleByAgeGroups(ageGroups)
                .stream()
                .map(res -> new ClusterCountAclResult(res.clusterName(), res.citizenCount()))
                .toList();
    }

    /**
     * Transforma un CitizenResource (REST DTO) a DemographicsCitizenResource (crude ACL resource).
     */
    private DemographicsCitizenResource toDemographicsResource(CitizenResource resource) {
        return new DemographicsCitizenResource(
                resource.citizenHash(),
                resource.incomeLevel(),
                resource.ageGroup(),
                resource.mobilityPattern(),
                resource.homeCluster()
        );
    }
}
