package tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipFocusArea;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipModality;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipTargetAudience;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate root que representa un programa público de mentoría del catálogo
 * AppBit (B2G).
 *
 * <p>Cada programa de mentoría describe una iniciativa pública (TECH, EMPLOYMENT,
 * HEALTH, CULTURE, EDUCATION, GENERAL) con su modalidad de entrega, público
 * objetivo y cluster geográfico al que aplica. Esta raíz es la
 * unidad de consistencia transaccional del BC mentorship: todos los cambios de
 * estado (cupos, activación, fechas) pasan por sus métodos de dominio, nunca
 * por setters públicos.</p>
 *
 * <p><b>Invariantes de dominio (garantizados por la raíz y por las CHECK
 * constraints de la V9)</b>:</p>
 * <ul>
 *   <li>{@code activeMentees} pertenece a {@code [0, totalCapacity]} cuando
 *       {@code totalCapacity} no es {@code null}; si es {@code null}, el cupo es
 *       ilimitado y solo se valida {@code activeMentees >= 0}.</li>
 *   <li>{@code endDate >= startDate} cuando ambos están presentes.</li>
 *   <li>{@code focusArea} y {@code modality} son siempre no nulos (validados por
 *       sus VOs en el factory).</li>
 *   <li>{@code targetAudience} es nullable; si no es {@code null}, debe ser del
 *       dominio cerrado.</li>
 * </ul>
 *
 * <p><b>Herencia</b>: extiende {@link AuditableAbstractAggregateRoot} que aporta
 * los timestamps de auditoría ({@code createdAt}, {@code updatedAt}) como
 * {@link java.time.Instant}, gestionados por Spring Data JPA Auditing.</p>
 */
@Getter
@Entity
@Table(name = "mentorship_programs", schema = "mentorship_schema")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MentorshipProgram extends AuditableAbstractAggregateRoot<MentorshipProgram> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "program_id", nullable = false, unique = true, length = 50)
    private String programId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "organization", length = 255)
    private String organization;

    @Embedded
    private MentorshipFocusArea focusArea;

    @Embedded
    private MentorshipModality modality;

    @Embedded
    private MentorshipTargetAudience targetAudience;

    @Column(name = "target_income_level", length = 10)
    private String targetIncomeLevel;

    @Column(name = "cluster_name", nullable = false, length = 100)
    private String clusterName;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "active_mentees", nullable = false)
    private int activeMentees;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    /**
     * Factory estático para crear un nuevo programa de mentoría a partir de un
     * comando validado.
     *
     * <p>Centraliza la construcción de los Value Objects desde los strings del
     * comando, garantizando las invariantes del agregado al instante de
     * creación. El estado por defecto es:</p>
     * <ul>
     *   <li>{@code activeMentees = 0} (sin mentees asignados al crear)</li>
     *   <li>{@code isActive = true} (el programa nace activo)</li>
     * </ul>
     *
     * @param command comando de creación con los datos crudos (strings) del programa
     * @return nueva instancia de {@link MentorshipProgram} en estado consistente
     * @throws IllegalArgumentException     si los campos críticos del comando son blank
     * @throws tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipFocusAreaException
     *                                       si {@code focusArea} es inválido
     * @throws tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipModalityException
     *                                       si {@code modality} es inválido
     * @throws tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipTargetAudienceException
     *                                       si {@code targetAudience} no es blank pero es inválido
     */
    public static MentorshipProgram create(CreateMentorshipProgramCommand command) {
        Objects.requireNonNull(command, "command no puede ser null");
        // Re-validación de contrato: el comando ya valida en su compact, pero este
        // factory puede ser llamado desde ACLs que no pasan por Bean Validation.
        MentorshipFocusArea focusArea = MentorshipFocusArea.of(command.focusArea());
        MentorshipModality modality = MentorshipModality.of(command.modality());
        MentorshipTargetAudience targetAudience = MentorshipTargetAudience.of(command.targetAudience());

        return new MentorshipProgram(
                null, // id generado por la BD
                command.programId(),
                command.name(),
                command.description(),
                command.organization(),
                focusArea,
                modality,
                targetAudience,
                command.targetIncomeLevel(),
                command.clusterName(),
                command.totalCapacity(),
                0, // activeMentees por defecto
                command.startDate(),
                command.endDate(),
                true, // isActive por defecto
                command.websiteUrl(),
                command.contactEmail()
        );
    }

    /**
     * Actualiza la capacidad total del programa.
     *
     * @param newTotalCapacity nueva capacidad total
     * @throws IllegalArgumentException si {@code newTotalCapacity < 0} o es
     *         menor que {@code activeMentees} (no se pueden "expulsar" mentees
     *         ya asignados bajando el cupo por debajo del ocupado)
     */
    public void updateCapacity(int newTotalCapacity) {
        if (newTotalCapacity < 0) {
            throw new IllegalArgumentException("totalCapacity debe ser >= 0");
        }
        if (newTotalCapacity < activeMentees) {
            throw new IllegalArgumentException(
                    String.format("totalCapacity (%d) no puede ser menor que activeMentees (%d)",
                            newTotalCapacity, activeMentees));
        }
        this.totalCapacity = newTotalCapacity;
    }

    /**
     * Inscribe un nuevo mentee en el programa.
     *
     * @throws IllegalStateException si el programa ya está sin capacidad disponible
     *         (solo cuando {@code totalCapacity != null})
     */
    public void enrollMentee() {
        if (totalCapacity != null && activeMentees >= totalCapacity) {
            throw new IllegalStateException(
                    String.format("Capacidad agotada: activeMentees=%d, totalCapacity=%d",
                            activeMentees, totalCapacity));
        }
        activeMentees++;
    }

    /**
     * Desinscribe un mentee del programa.
     *
     * @throws IllegalStateException si {@code activeMentees == 0} (no hay mentees
     *         que desinscribir)
     */
    public void unenrollMentee() {
        if (activeMentees <= 0) {
            throw new IllegalStateException(
                    "No se puede desinscribir un mentee: activeMentees=0");
        }
        activeMentees--;
    }

    /**
     * Desactiva el programa y registra la fecha de cierre como hoy.
     */
    public void deactivate() {
        this.isActive = false;
        this.endDate = LocalDate.now();
    }

    /**
     * Reactiva el programa previamente desactivado.
     */
    public void activate() {
        this.isActive = true;
    }
}
