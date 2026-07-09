package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.adapter.InclusionCoreInternalClient;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.transform.InclusionCoreErrorAssembler;

import java.util.List;

/**
 * ACL Facade for the Inclusion Core bounded context — mentorship side.
 *
 * <p>Implements {@link InclusionCoreAclPort} and provides the mentorship BC
 * with vulnerability data from inclusioncore. Delegates the transport to
 * {@link InclusionCoreInternalClient} (direct in-process injection, no HTTP)
 * and translates errors via {@link InclusionCoreErrorAssembler}.</p>
 *
 * <p>This facade follows the same pattern as
 * {@code inclusioncore.interfaces.acl.telemetry.TelemetryAclFacade}:</p>
 * <ul>
 *   <li>{@code @Component @Slf4j @RequiredArgsConstructor}</li>
 *   <li>Implements a consumer-side Port</li>
 *   <li>Injects the adapter (transport layer)</li>
 *   <li>Wraps calls in try/catch → ErrorAssembler</li>
 * </ul>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class InclusionCoreAclFacade implements InclusionCoreAclPort {

    private final InclusionCoreInternalClient adapter;

    @Override
    public List<VulnerableClusterAclResult> getVulnerableRegions(
            int minScore,
            int maxResults,
            boolean poorConnectivityOnly
    ) {
        try {
            log.info("Obteniendo regiones vulnerables de inclusioncore: minScore={}, maxResults={}, poorOnly={}",
                    minScore, maxResults, poorConnectivityOnly);
            return adapter.getVulnerableRegions(minScore, maxResults, poorConnectivityOnly);
        } catch (Exception ex) {
            throw InclusionCoreErrorAssembler.assemble(ex,
                    String.format("minScore=%d, maxResults=%d, poorOnly=%s",
                            minScore, maxResults, poorConnectivityOnly));
        }
    }
}