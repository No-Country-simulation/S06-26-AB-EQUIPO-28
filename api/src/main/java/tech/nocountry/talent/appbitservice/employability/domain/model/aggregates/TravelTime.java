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
 * Aggregate root que representa un registro de tiempos de viaje inter-cluster
 * del dataset CDRView ({@code tensor_tempo_deslocamento.csv}) para el BC
 * employability.
 *
 * <p>Cada fila describe las observaciones de viaje entre un cluster de origen
 * y uno de destino: número de observaciones, distancia promedio y sus
 * percentiles 25 y 75, junto con el período de sesión predominante. Al igual
 * que {@link MobilityODPair}, es un agregado de <b>lectura</b> cargado durante
 * la ingesta del CSV: no expone lógica de negocio writable.</p>
 *
 * <p><b>Invariantes de dominio (garantizados por la raíz y por las CHECK
 * constraints de la V11)</b>:</p>
 * <ul>
 *   <li>{@code originCluster} y {@code destinationCluster} son no nulos.</li>
 *   <li>{@code predominantPeriod} es un {@link SessionPeriod} válido.</li>
 *   <li>{@code observations >= 0}, {@code averageDistanceKm >= 0}.</li>
 *   <li>{@code p25DistanceKm} y {@code p75DistanceKm} son nullable.</li>
 * </ul>
 *
 * <p><b>Herencia</b>: extiende {@link AuditableAbstractAggregateRoot} que aporta
 * los timestamps de auditoría gestionados por Spring Data JPA Auditing.</p>
 */
@Getter
@Entity
@Table(name = "travel_times", schema = "employability_schema")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TravelTime extends AuditableAbstractAggregateRoot<TravelTime> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "origin_cluster", nullable = false, length = 100)
    private String originCluster;

    @Column(name = "destination_cluster", nullable = false, length = 100)
    private String destinationCluster;

    @Column(name = "same_cluster", nullable = false)
    private boolean sameCluster;

    @Column(name = "observations", nullable = false)
    private int observations;

    @Column(name = "avg_distance_km", nullable = false)
    private double averageDistanceKm;

    @Column(name = "p25_distance_km")
    private Double p25DistanceKm;

    @Column(name = "p75_distance_km")
    private Double p75DistanceKm;

    @Column(name = "predominant_period", nullable = false, length = 20)
    private String predominantPeriod;

    /**
     * Factory estático para crear un nuevo registro de tiempo de viaje
     * inter-cluster a partir de los datos crudos del CSV CDRView.
     *
     * <p>Valida los campos NOT NULL ({@code originCluster},
     * {@code destinationCluster}, {@code predominantPeriod}) y que el período
     * sea un {@link SessionPeriod} válido.</p>
     *
     * @param originCluster      cluster de origen (no nulo)
     * @param destCluster        cluster de destino (no nulo)
     * @param sameCluster        {@code true} si origen y destino son el mismo cluster
     * @param observations       número de observaciones (>= 0)
     * @param avgDistanceKm      distancia promedio en km (>= 0)
     * @param p25DistanceKm      percentil 25 de distancia en km (nullable)
     * @param p75DistanceKm      percentil 75 de distancia en km (nullable)
     * @param predominantPeriod  período predominante (DAWN, MORNING, AFTERNOON, NIGHT)
     * @return nueva instancia de {@link TravelTime} consistente
     * @throws IllegalArgumentException                                                 si los campos críticos son blank
     * @throws tech.nocountry.talent.appbitservice.employability.domain.exceptions.InvalidSessionPeriodException
     *                                                                                  si {@code predominantPeriod} es inválido
     */
    public static TravelTime create(
            String originCluster, String destCluster, boolean sameCluster,
            int observations, double avgDistanceKm,
            Double p25DistanceKm, Double p75DistanceKm,
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

        return new TravelTime(
                null, // id generado por la BD
                originCluster.trim(),
                destCluster.trim(),
                sameCluster,
                observations,
                avgDistanceKm,
                p25DistanceKm,
                p75DistanceKm,
                predominantPeriod.toUpperCase().trim()
        );
    }
}