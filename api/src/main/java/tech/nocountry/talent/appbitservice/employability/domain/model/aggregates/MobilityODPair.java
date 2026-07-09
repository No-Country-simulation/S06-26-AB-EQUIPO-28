package tech.nocountry.talent.appbitservice.employability.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects.SessionPeriod;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root que representa un par origen-destino (OD) de movilidad del
 * dataset CDRView ({@code tensor_od.csv}) para el BC employability.
 *
 * <p>Cada fila describe el flujo observado entre un cluster de origen y uno de
 * destino: cuántos usuarios únicos lo recorrieron, cuántos viajes en total, la
 * distancia promedio (Haversine) y el período de sesión predominante. Es un
 * agregado de <b>lectura</b> cargado durante la ingesta del CSV: no expone
 * lógica de negocio writable, solo la invariante de consistencia de sus campos
 * críticos garantizada en el factory {@link #create}.</p>
 *
 * <p><b>Invariantes de dominio (garantizados por la raíz y por las CHECK
 * constraints de la V11)</b>:</p>
 * <ul>
 *   <li>{@code originCluster} y {@code destinationCluster} son no nulos.</li>
 *   <li>{@code predominantPeriod} es un {@link SessionPeriod} válido
 *       (DAWN, MORNING, AFTERNOON, NIGHT).</li>
 *   <li>{@code uniqueUsers >= 0}, {@code totalTrips >= 0},
 *       {@code averageDistanceKm >= 0}.</li>
 * </ul>
 *
 * <p><b>Herencia</b>: extiende {@link AuditableAbstractAggregateRoot} que aporta
 * los timestamps de auditoría ({@code createdAt}, {@code updatedAt}) gestionados
 * por Spring Data JPA Auditing.</p>
 *
 * <p><b>Nota de mapeo</b>: los clusters se persisten como {@code String} directo
 * (no como {@code @Embedded ClusterRef}) para simplificar la carga desde el CSV.
 * Si se desea validar la referencia, usar {@code ClusterRef.of()} que lanza
 * {@link tech.nocountry.talent.appbitservice.employability.domain.exceptions.InvalidClusterException}.</p>
 */
@Getter
@Entity
@Table(name = "mobility_od_pairs", schema = "employability_schema")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MobilityODPair extends AuditableAbstractAggregateRoot<MobilityODPair> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "origin_cluster", nullable = false, length = 100)
    private String originCluster;

    @Column(name = "origin_municipio", length = 100)
    private String originMunicipio;

    @Column(name = "origin_latitude")
    private double originLatitude;

    @Column(name = "origin_longitude")
    private double originLongitude;

    @Column(name = "destination_cluster", nullable = false, length = 100)
    private String destinationCluster;

    @Column(name = "destination_municipio", length = 100)
    private String destinationMunicipio;

    @Column(name = "destination_latitude")
    private double destinationLatitude;

    @Column(name = "destination_longitude")
    private double destinationLongitude;

    @Column(name = "same_cluster", nullable = false)
    private boolean sameCluster;

    @Column(name = "unique_users", nullable = false)
    private int uniqueUsers;

    @Column(name = "total_trips", nullable = false)
    private int totalTrips;

    @Column(name = "avg_distance_km", nullable = false)
    private double averageDistanceKm;

    @Column(name = "predominant_period", nullable = false, length = 20)
    private String predominantPeriod;

    /**
     * Factory estático para crear un nuevo par OD de movilidad a partir de los
     * datos crudos del CSV CDRView.
     *
     * <p>Valida los campos NOT NULL ({@code originCluster},
     * {@code destinationCluster}, {@code predominantPeriod}) y que el período
     * sea un {@link SessionPeriod} válido. Los campos geográficos y de conteo
     * no se validan aquí salvo por no-nulidad de los críticos: las CHECK
     * constraints de la BD (V11) garantizan los rangos {@code >= 0} en
     * persistencia.</p>
     *
     * @param originCluster      cluster de origen (no nulo)
     * @param originMunicipio    municipio de origen (nullable)
     * @param originLat          latitud del origen
     * @param originLon          longitud del origen
     * @param destCluster        cluster de destino (no nulo)
     * @param destMunicipio       municipio de destino (nullable)
     * @param destLat            latitud del destino
     * @param destLon            longitud del destino
     * @param sameCluster        {@code true} si origen y destino son el mismo cluster
     * @param uniqueUsers        usuarios únicos que recorrieron el par (>= 0)
     * @param totalTrips         viajes totales observados (>= 0)
     * @param avgDistanceKm      distancia promedio en km (>= 0)
     * @param predominantPeriod  período predominante (DAWN, MORNING, AFTERNOON, NIGHT)
     * @return nueva instancia de {@link MobilityODPair} consistente
     * @throws IllegalArgumentException                                                 si los campos críticos son blank
     * @throws tech.nocountry.talent.appbitservice.employability.domain.exceptions.InvalidSessionPeriodException
     *                                                                                  si {@code predominantPeriod} es inválido
     */
    public static MobilityODPair create(
            String originCluster, String originMunicipio, double originLat, double originLon,
            String destCluster, String destMunicipio, double destLat, double destLon,
            boolean sameCluster, int uniqueUsers, int totalTrips, double avgDistanceKm,
            String predominantPeriod) {
        if (originCluster == null || originCluster.isBlank()) {
            throw new IllegalArgumentException("originCluster no puede estar vacío");
        }
        if (destCluster == null || destCluster.isBlank()) {
            throw new IllegalArgumentException("destinationCluster no puede estar vacío");
        }
        Objects.requireNonNull(predominantPeriod, "predominantPeriod no puede ser null");
        // Valida que el período sea un SessionPeriod válido (lanza si inválido).
        SessionPeriod.fromString(predominantPeriod);

        return new MobilityODPair(
                null, // id generado por la BD
                originCluster.trim(),
                originMunicipio,
                originLat,
                originLon,
                destCluster.trim(),
                destMunicipio,
                destLat,
                destLon,
                sameCluster,
                uniqueUsers,
                totalTrips,
                avgDistanceKm,
                predominantPeriod.toUpperCase().trim()
        );
    }
}