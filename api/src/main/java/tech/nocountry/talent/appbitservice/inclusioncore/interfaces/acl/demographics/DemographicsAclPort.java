package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics;

import java.util.List;

/**
 * ACL Port para el contexto Demográfico.
 *
 * <p>Esta interfaz es consumida por los casos de uso del bounded context inclusion-core.
 * Abstrae el origen de datos demográficos usando únicamente el lenguaje del dominio de inclusión.</p>
 *
 * <p>El bounded context inclusion-core depende SOLO de esta interfaz, nunca directamente
 * del modelo del bounded context demographics.</p>
 *
 * <p>Implementación: DemographicsAclFacade (orquesta adapter + assembler).</p>
 */
public interface DemographicsAclPort {
    /**
     * Obtiene los ciudadanos vulnerables (nivel de ingreso D).
     *
     * <p>Útil para identificar población que requiere programas de inclusión social.</p>
     *
     * @return lista de ciudadanos en situación de vulnerabilidad
     */
    List<DemographicsAclResult> getVulnerableCitizens();

    /**
     * Gets citizen counts grouped by home cluster, with optional income level filter.
     *
     * @param incomeLevel optional income level filter (e.g., "D" for vulnerable). Null returns all clusters.
     * @return list of cluster count results
     */
    List<ClusterCountAclResult> getCitizenCountByCluster(String incomeLevel);

    /**
     * Gets citizen counts grouped by home cluster, filtered by mobility pattern.
     *
     * @param pattern the mobility pattern (e.g., "LOW" for social isolation proxy)
     * @return list of cluster count results
     */
    List<ClusterCountAclResult> getCitizenCountByClusterAndMobilityPattern(String pattern);

    /**
     * Gets citizen counts grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the vulnerable age groups (e.g., "18-24", "55+")
     * @return list of cluster count results
     */
    List<ClusterCountAclResult> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups);
}
