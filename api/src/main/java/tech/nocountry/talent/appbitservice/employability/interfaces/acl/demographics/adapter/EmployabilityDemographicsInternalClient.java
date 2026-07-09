package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.GetCitizensByClusterEndpoint;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.EmployabilityClusterCitizenSummary;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.resources.EmployabilityClusterCitizenCountRawResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.transform.EmployabilityDemographicsExternalResourceAssembler;

import java.util.List;

/**
 * Adapter that consumes the demographics BC internal endpoint via direct
 * in-process injection (Gastro Suite pattern for modular monoliths).
 *
 * <p>No HTTP, no REST clients: it injects the {@code @Component}
 * {@link GetCitizensByClusterEndpoint} and, crucially, never touches the
 * upstream demographics resource shape directly.
 * {@link EmployabilityDemographicsExternalResourceAssembler} (MapStruct) translates the
 * upstream resource into the employability-owned raw resource
 * ({@link EmployabilityClusterCitizenCountRawResource}); this adapter then builds the
 * employability ubiquitous language record ({@link EmployabilityClusterCitizenSummary}).
 * This is the only place in the employability BC that reaches into the
 * demographics internal endpoint; everything upstream of this adapter only
 * sees the employability ACL results.</p>
 *
 * <p>The upstream {@link GetCitizensByClusterEndpoint#handle(String)} already
 * returns counts grouped by cluster, so no in-memory aggregation is required:
 * the adapter is a pure translator over the local raw resources.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployabilityDemographicsInternalClient {

    private final GetCitizensByClusterEndpoint getCitizensByClusterEndpoint;
    private final EmployabilityDemographicsExternalResourceAssembler demographicsExternalResourceAssembler;

    /**
     * Gets citizen counts grouped by home cluster with an optional income level
     * filter.
     *
     * @param incomeLevel optional income level filter (nullable; null returns all clusters)
     * @return list of cluster citizen summaries in the employability language
     */
    public List<EmployabilityClusterCitizenSummary> getCitizenCountByCluster(String incomeLevel) {
        log.debug("Fetching citizen count by cluster via direct invocation, incomeLevel={}", incomeLevel);
        List<EmployabilityClusterCitizenCountRawResource> raws = demographicsExternalResourceAssembler.toRawList(
                getCitizensByClusterEndpoint.handle(incomeLevel));
        return raws.stream()
                .map(raw -> new EmployabilityClusterCitizenSummary(
                        raw.clusterName(),
                        raw.citizenCount() == null ? 0L : raw.citizenCount()
                ))
                .toList();
    }

    /**
     * Gets citizen counts grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the age groups to include (e.g., "18-24", "55+")
     * @return list of cluster citizen summaries in the employability language
     */
    public List<EmployabilityClusterCitizenSummary> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups) {
        log.debug("Fetching citizen count by cluster and age groups via direct invocation, ageGroups={}", ageGroups);
        List<EmployabilityClusterCitizenCountRawResource> raws = demographicsExternalResourceAssembler.toRawList(
                getCitizensByClusterEndpoint.handleByAgeGroups(ageGroups));
        return raws.stream()
                .map(raw -> new EmployabilityClusterCitizenSummary(
                        raw.clusterName(),
                        raw.citizenCount() == null ? 0L : raw.citizenCount()
                ))
                .toList();
    }
}