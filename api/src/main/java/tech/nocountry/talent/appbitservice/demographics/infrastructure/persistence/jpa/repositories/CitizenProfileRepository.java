package tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates.CitizenProfile;
import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.CitizenId;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad CitizenProfile.
 *
 * <p>Proporciona métodos de acceso a datos para perfiles de ciudadanos.
 * Extiende {@link JpaSpecificationExecutor} para permitir consultas dinámicas
 * con {@link org.springframework.data.jpa.domain.Specification}.</p>
 */
@Repository
public interface CitizenProfileRepository extends JpaRepository<CitizenProfile, CitizenId>,
        JpaSpecificationExecutor<CitizenProfile> {

    /**
     * Projection for citizen count grouped by cluster.
     */
    interface ClusterCountProjection {
        String getClusterName();
        Long getCitizenCount();
    }
    /**
     * Busca un perfil por el valor del identificador.
     *
     * @param citizenIdValue el valor del identificador del ciudadano
     * @return Optional con el perfil si existe
     */
    Optional<CitizenProfile> findByCitizenIdValue(String citizenIdValue);

    /**
     * Busca todos los perfiles con un nivel de ingreso específico.
     *
     * @param incomeLevel el nivel de ingreso (A, B, C, D)
     * @return lista de perfiles que coinciden
     */
    List<CitizenProfile> findByIncomeLevelValue(String incomeLevel);

    /**
     * Busca todos los perfiles con un grupo de edad específico.
     *
     * @param ageGroup el grupo de edad
     * @return lista de perfiles que coinciden
     */
    List<CitizenProfile> findByAgeGroupValue(String ageGroup);

    /**
     * Counts citizens grouped by home cluster, with optional income-level filter.
     *
     * @param incomeLevel the income level to filter by (nullable, e.g. "A", "B", "C", "D")
     * @return list of cluster counts sorted descending
     */
    @Query("SELECT c.homeCluster.value AS clusterName, COUNT(c) AS citizenCount " +
           "FROM CitizenProfile c " +
           "WHERE (:incomeLevel IS NULL OR c.incomeLevel.value = :incomeLevel) " +
           "GROUP BY c.homeCluster.value " +
           "ORDER BY citizenCount DESC")
    List<ClusterCountProjection> countByIncomeLevelGroupByCluster(@Param("incomeLevel") String incomeLevel);

    /**
     * Counts citizens grouped by home cluster, filtered by mobility pattern.
     *
     * @param pattern the mobility pattern (e.g., "LOW" for social isolation proxy)
     * @return list of cluster counts sorted descending
     */
    @Query("SELECT c.homeCluster.value AS clusterName, COUNT(c) AS citizenCount " +
           "FROM CitizenProfile c " +
           "WHERE c.mobilityPattern.value = :pattern " +
           "GROUP BY c.homeCluster.value " +
           "ORDER BY citizenCount DESC")
    List<ClusterCountProjection> countByMobilityPatternGroupByCluster(@Param("pattern") String pattern);

    /**
     * Counts citizens grouped by home cluster, filtered by age groups.
     *
     * @param ageGroups the age groups to include (e.g., "18-24", "55+")
     * @return list of cluster counts sorted descending
     */
    @Query("SELECT c.homeCluster.value AS clusterName, COUNT(c) AS citizenCount " +
           "FROM CitizenProfile c " +
           "WHERE c.ageGroup.value IN :ageGroups " +
           "GROUP BY c.homeCluster.value " +
           "ORDER BY citizenCount DESC")
    List<ClusterCountProjection> countByAgeGroupInGroupByCluster(@Param("ageGroups") List<String> ageGroups);
}