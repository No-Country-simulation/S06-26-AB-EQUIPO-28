package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore;

import java.util.List;

/**
 * ACL Port for the Inclusion Core bounded context — mentorship language.
 *
 * <p>Declares the contract the mentorship BC needs from inclusioncore to
 * compute mentorship gap analysis. The facade implementing this port will
 * consume {@code HealthVulnerabilityInternalEndpoint} via direct in-process
 * injection (Gastro Suite pattern, no HTTP, no new ACL to demographics/telemetry).</p>
 *
 * <p>This is the mentorship-side interface (consumer-side port) that the
 * mentorship BC depends on. It lives in {@code interfaces/acl/inclusioncore/}
 * and is implemented by {@link InclusionCoreAclFacade}.</p>
 */
public interface InclusionCoreAclPort {

    /**
     * Retrieves vulnerable geographic regions from the inclusion core BC.
     *
     * <p>The upstream calculates vulnerability scores on-the-fly by crossing
     * demographic and telemetry data. Results are returned in the mentorship
     * BC's own language via {@link VulnerableClusterAclResult}.</p>
     *
     * @param minScore minimum vulnerability score threshold (0-100)
     * @param maxResults maximum number of results to return
     * @param poorConnectivityOnly if {@code true}, filter to regions with poor connectivity only
     * @return list of vulnerable clusters in mentorship language
     */
    List<VulnerableClusterAclResult> getVulnerableRegions(int minScore, int maxResults, boolean poorConnectivityOnly);
}