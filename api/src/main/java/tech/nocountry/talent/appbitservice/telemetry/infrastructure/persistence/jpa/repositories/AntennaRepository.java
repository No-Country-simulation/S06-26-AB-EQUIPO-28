package tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates.Antenna;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.ClusterName;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.Ecgi;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Antenna.
 *
 * <p>Proporciona métodos de acceso a datos para las antenas de telecomunicación.
 * Utiliza el esquema {@code telemetry_schema} definido en Flyway.</p>
 */
@Repository
public interface AntennaRepository extends JpaRepository<Antenna, Ecgi> {
    /**
     * Busca todas las antenas.
     *
     * @return lista de todas las antenas
     */
    List<Antenna> findAll();

    /**
     * Busca una antenna por su identificador ECGI.
     *
     * @param ecgiValue identificador único de la celda
     * @return Optional conteniendo la antenna si existe
     */
    @Query("SELECT a FROM Antenna a WHERE a.ecgi.value = :ecgiValue")
    Optional<Antenna> findByEcgiValue(@Param("ecgiValue") String ecgiValue);

    /**
     * Busca todas las antenas que pertenecen a un cluster geográfico.
     *
     * @param cluster nombre del cluster geográfico
     * @return lista de antenas en el cluster especificado
     */
    List<Antenna> findByCluster(ClusterName cluster);
}