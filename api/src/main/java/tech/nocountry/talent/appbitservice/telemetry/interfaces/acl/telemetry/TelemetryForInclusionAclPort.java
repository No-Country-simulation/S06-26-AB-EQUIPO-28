package tech.nocountry.talent.appbitservice.telemetry.interfaces.acl.telemetry;

import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Puerto ACL para exponer datos de telemetría al contexto de inclusión social (inclusion-core).
 *
 * <p>Define el contrato de consumo para que el core domain de inclusión social pueda
 * acceder a datos de infraestructura de red, concentración y antenas sin acoplarse
 * directamente al modelo de telemetría.</p>
 *
 * <p>Sigue el patrón de Puerto-Adaptador de Gastro Suite:
 * {@code BranchForZoneAclPort}.</p>
 */
public interface TelemetryForInclusionAclPort {

    /**
     * Obtiene todas las antenas del sistema.
     *
     * @return lista de recursos de antenas
     */
    List<AntennaResource> findAllAntennas();

    /**
     * Obtiene las métricas de concentración con baja conectividad (drop &gt; 5%).
     *
     * <p>Útil para identificar zonas con infraestructura deficiente en el análisis
     * de brecha digital.</p>
     *
     * @return lista de recursos de concentración con baja conectividad
     */
    List<ConcentrationResource> findLowConnectivityConcentrations();

    /**
     * Obtiene las métricas de concentración para un cluster geográfico específico.
     *
     * @param clusterName nombre del cluster geográfico
     * @return lista de recursos de concentración del cluster
     */
    List<ConcentrationResource> findConcentrationsByCluster(String clusterName);
}
