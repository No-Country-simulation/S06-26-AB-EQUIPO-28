package tech.nocountry.talent.appbitservice.telemetry.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.UuidV7Generator;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.commands.CreateConcentrationCommand;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.ConcentrationMetrics;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.Ecgi;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.SessionPeriod;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad que representa las métricas de concentración de red por celda y período.
 *
 * <p>Almacena los datos de tráfico de telecomunicaciones agregados por:</p>
 * <ul>
 *   <li>Identificador de celda (ECGI)</li>
 *   <li>Fecha del día</li>
 *   <li>Período de la sesión</li>
 * </ul>
 *
 * <p>Esta entidad contiene métricas como número de usuarios, sesiones, bytes transferidos,
 * llamadas, mensajes, y métricas de calidad de red (drop y congestión).</p>
 *
 * <p>Anteriormente conocida como NetworkConcentracion.</p>
 */
@Getter
@Entity
@Table(name = "network_concentration", schema = "telemetry_schema")
@NoArgsConstructor
public class NetworkConcentration extends AuditableAbstractAggregateRoot<NetworkConcentration> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)  // CSV: id
    private UUID id;

    @Embedded
    private Ecgi ecgi;  // CSV: ecgi

    @Column(name = "day_date", nullable = false)  // CSV: day_date
    private LocalDate dayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_period", nullable = false, length = 20)  // CSV: periodo
    private SessionPeriod sessionPeriod;

    @Column(name = "user_count")  // CSV: n_usuarios
    private Integer userCount;

    @Column(name = "session_count")  // CSV: n_sessoes
    private Integer sessionCount;

    @Column(name = "download_bytes")  // CSV: download_bytes
    private Long downloadBytes;

    @Column(name = "upload_bytes")  // CSV: upload_bytes
    private Long uploadBytes;

    @Column(name = "average_duration_s")  // CSV: dur_media_s
    private Double averageDurationS;

    @Embedded
    private ConcentrationMetrics concentrationMetrics;  // CSV: drop_pct_medio, congestionamento_medio

    @Column(name = "total_calls")  // CSV: chamadas_total
    private Long totalCalls;

    @Column(name = "total_messages")  // CSV: mensagens_total
    private Long totalMessages;

    @Column(name = "latitude")  // CSV: lat
    private Double latitude;

    @Column(name = "longitude")  // CSV: lon
    private Double longitude;

    /**
     * Constructor que recibe el comando de creación.
     *
     * @param command comando con los datos de concentración
     */
    public NetworkConcentration(CreateConcentrationCommand command) {
        this.id = UuidV7Generator.generate();
        this.ecgi = command.ecgi();
        this.dayDate = command.dayDate();
        this.sessionPeriod = command.period();
        this.userCount = command.userCount();
        this.sessionCount = command.sessionCount();
        this.downloadBytes = command.downloadBytes();
        this.uploadBytes = command.uploadBytes();
        this.averageDurationS = command.averageDurationS();
        this.concentrationMetrics = command.concentrationMetrics();
        this.totalCalls = command.totalCalls();
        this.totalMessages = command.totalMessages();
        this.latitude = command.latitude();
        this.longitude = command.longitude();
    }

    /**
     * Constructor estático factory para crear una concentración.
     *
     * @param command comando con los datos de concentración
     * @return nueva instancia de NetworkConcentration
     */
    public static NetworkConcentration create(CreateConcentrationCommand command) {
        return new NetworkConcentration(command);
    }
}