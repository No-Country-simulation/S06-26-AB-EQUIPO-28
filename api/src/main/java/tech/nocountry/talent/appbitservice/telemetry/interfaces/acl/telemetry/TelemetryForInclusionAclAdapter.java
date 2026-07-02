package tech.nocountry.talent.appbitservice.telemetry.interfaces.acl.telemetry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna.GetAllAntennasEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration.GetConcentrationFilteredEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration.GetLowConnectivityConcentrationEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Adaptador ACL que utiliza los Internal Endpoints de Telemetría.
 *
 * <p>Traduce las llamadas del contexto de inclusión social a los endpoints internos del
 * módulo de telemetría. Este adaptador es la capa de integración concreta que previene
 * el acoplamiento directo entre bounded contexts.</p>
 *
 * <p>Sigue el patrón de Gastro Suite:
 * {@code BranchForZoneAclAdapter}.</p>
 */
@Component
@RequiredArgsConstructor
public class TelemetryForInclusionAclAdapter {
    private final GetAllAntennasEndpoint getAllAntennasEndpoint;
    private final GetConcentrationFilteredEndpoint getConcentrationFilteredEndpoint;
    private final GetLowConnectivityConcentrationEndpoint getLowConnectivityConcentrationEndpoint;

    public List<AntennaResource> findAllAntennas() {
        return getAllAntennasEndpoint.handleAll().content();
    }

    public List<ConcentrationResource> findLowConnectivityConcentrations() {
        return getLowConnectivityConcentrationEndpoint.handle(0.05);
    }

    public List<ConcentrationResource> findConcentrationsByCluster(String clusterName) {
        return getConcentrationFilteredEndpoint.handleAll(clusterName).content();
    }

    public List<ConcentrationResource> findAllConcentrations() {
        return getConcentrationFilteredEndpoint.handleAll().content();
    }
}
