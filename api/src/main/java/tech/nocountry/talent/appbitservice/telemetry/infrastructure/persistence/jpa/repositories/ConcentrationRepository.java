package tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.entities.NetworkConcentration;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.ClusterName;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad NetworkConcentration.
 *
 * <p>Proporciona métodos de acceso a datos para las métricas de concentración de red.
 * Utiliza el esquema {@code telemetry_schema} definido en Flyway.</p>
 *
 * <p>Anteriormente conocido como ConcentracionRepository.</p>
 */
@Repository
public interface ConcentrationRepository extends JpaRepository<NetworkConcentration, UUID>,
        JpaSpecificationExecutor<NetworkConcentration> {
    /**
     * Busca todas las concentraciones de un cluster geográfico.
     *
     * @param cluster nombre del cluster geográfico
     * @return lista de concentraciones en el cluster especificado
     */
    @Query("""
            SELECT c FROM NetworkConcentration c
            JOIN Antenna a ON c.ecgi.value = a.ecgi.value
            WHERE a.cluster = :cluster
            """)
    List<NetworkConcentration> findByCluster(@Param("cluster") ClusterName cluster);

}