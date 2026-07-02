package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry;

import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.RegionMetric;

import java.util.List;

/**
 * ACL Port para el contexto de Telemetría.
 *
 * <p>Esta interfaz es consumida por los casos de uso del bounded context inclusion-core.
 * Abstrae el origen de datos de telemetría usando únicamente el lenguaje del dominio de inclusión.</p>
 *
 * <p>El bounded context inclusion-core depende SOLO de esta interfaz, nunca directamente
 * del modelo del bounded context telemetry.</p>
 *
 * <p>Implementación: TelemetryAclFacade (orquesta adapter + assembler).</p>
 */
public interface TelemetryAclPort {
    /**
     * Obtiene las métricas de concentration por cluster geográfico.
     *
     * @param cluster el nombre del cluster geográfico
     * @return lista de métricas de región para el cluster especificado
     */
    List<RegionMetric> getConcentrationByCluster(String cluster);

    /**
     * Obtiene todas las métricas de concentration disponibles.
     *
     * @return lista completa de métricas de región
     */
    List<RegionMetric> getAllConcentration();
}
