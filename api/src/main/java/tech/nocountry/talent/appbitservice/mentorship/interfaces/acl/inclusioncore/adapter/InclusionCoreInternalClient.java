package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.internal.healthvulnerability.HealthVulnerabilityInternalEndpoint;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.VulnerableClusterAclResult;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.transform.VulnerableClusterAssembler;

import java.util.List;

/**
 * Internal client adapter that consumes the inclusioncore BC via direct
 * in-process injection (Gastro Suite pattern for modular monoliths).
 *
 * <p>No HTTP, no REST clients — just a plain {@code @Component} that injects
 * {@link HealthVulnerabilityInternalEndpoint} and translates the upstream
 * resources into mentorship language via {@link VulnerableClusterAssembler}.</p>
 *
 * <p>This adapter lives at the transport boundary and is injected by
 * {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.InclusionCoreAclFacade}.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InclusionCoreInternalClient {

    private final HealthVulnerabilityInternalEndpoint healthVulnerabilityInternalEndpoint;
    private final VulnerableClusterAssembler assembler;

    /**
     * Retrieves vulnerable regions from the inclusioncore BC and translates
     * them into mentorship's {@link VulnerableClusterAclResult} language.
     *
     * @param minScore minimum vulnerability score threshold (0-100)
     * @param maxResults maximum number of results to return
     * @param poorConnectivityOnly if {@code true}, filter to regions with poor connectivity only
     * @return list of vulnerable clusters in mentorship language
     */
    public List<VulnerableClusterAclResult> getVulnerableRegions(
            int minScore,
            int maxResults,
            boolean poorConnectivityOnly
    ) {
        log.debug("Fetching vulnerable regions from inclusioncore: minScore={}, maxResults={}, poorOnly={}",
                minScore, maxResults, poorConnectivityOnly);

        var upstreamResources = healthVulnerabilityInternalEndpoint.getVulnerableRegions(
                minScore, maxResults, poorConnectivityOnly
        );

        var results = assembler.toAclResultList(upstreamResources);

        log.debug("Translated {} vulnerable regions from inclusioncore to mentorship language",
                results.size());

        return results;
    }
}