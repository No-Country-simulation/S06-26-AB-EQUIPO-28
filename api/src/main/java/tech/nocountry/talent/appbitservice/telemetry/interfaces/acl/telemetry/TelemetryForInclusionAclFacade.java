package tech.nocountry.talent.appbitservice.telemetry.interfaces.acl.telemetry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Fachada ACL para exponer datos de telemetría al contexto de inclusión social.
 *
 * <p>Implementa el puerto {@link TelemetryForInclusionAclPort} y delega la ejecución
 * al {@link TelemetryForInclusionAclAdapter}. Es la fachada pública que los consumidores
 * (inclusion-core) inyectan.</p>
 *
 * <p>Sigue el patrón Facade-Delegator de Gastro Suite:
 * {@code BranchForZoneAclFacade}.</p>
 */
@Component
@RequiredArgsConstructor
public class TelemetryForInclusionAclFacade implements TelemetryForInclusionAclPort {
    private final TelemetryForInclusionAclAdapter adapter;

    @Override
    public List<AntennaResource> findAllAntennas() {
        return adapter.findAllAntennas();
    }

    @Override
    public List<ConcentrationResource> findLowConnectivityConcentrations() {
        return adapter.findLowConnectivityConcentrations();
    }

    @Override
    public List<ConcentrationResource> findConcentrationsByCluster(String clusterName) {
        return adapter.findConcentrationsByCluster(clusterName);
    }
}
