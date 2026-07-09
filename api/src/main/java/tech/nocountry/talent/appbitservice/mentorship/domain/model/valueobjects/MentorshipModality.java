package tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipModalityException;

/**
 * Value Object que representa la modalidad de entrega de un programa de mentoría.
 *
 * <p>Dominio cerrado de valores permitidos:</p>
 * <ul>
 *   <li>{@code REMOTE} - Mentoría remota (online / telepresencial)</li>
 *   <li>{@code IN_PERSON} - Mentoría presencial</li>
 *   <li>{@code HYBRID} - Mentoría híbrida (presencial + remota)</li>
 * </ul>
 *
 * <p>La modalidad es clave para evaluar viabilidad en zonas con baja conectividad:
 * un programa {@code REMOTE} en un cluster con {@code ConnectivityLevel.LOW} es
 * candidato a ser marcado como brecha por el inclusion-core.</p>
 *
 * <p>Sigue el patrón del {@code ConnectivityLevel} del bounded context de
 * inclusion-core.</p>
 */
@Embeddable
public record MentorshipModality(
        @Column(name = "modality", nullable = false)
        String value
) {
    public static final String REMOTE = "REMOTE";
    public static final String IN_PERSON = "IN_PERSON";
    public static final String HYBRID = "HYBRID";

    /**
     * Constructor compact: normaliza y valida el valor.
     *
     * @throws InvalidMentorshipModalityException si el valor es nulo, vacío
     *         o no corresponde a ninguno de los valores permitidos.
     */
    public MentorshipModality {
        if (value == null || value.isBlank()) {
            throw new InvalidMentorshipModalityException(
                    "La modalidad de la mentoría no puede estar vacía"
            );
        }
        String normalized = value.toUpperCase().trim();
        if (!isValid(normalized)) {
            throw new InvalidMentorshipModalityException(
                    String.format("Modalidad inválida: %s. Válidas: REMOTE, IN_PERSON, HYBRID", value)
            );
        }
        value = normalized;
    }

    private static boolean isValid(String normalized) {
        return REMOTE.equals(normalized)
                || IN_PERSON.equals(normalized)
                || HYBRID.equals(normalized);
    }

    /**
     * Factory que crea un {@link MentorshipModality} a partir de un string.
     *
     * <p>La normalización (trim + uppercase) es case-insensitive.</p>
     *
     * @param modality la modalidad (e.g., "remote", "IN_PERSON")
     * @return una nueva instancia de {@link MentorshipModality}
     * @throws InvalidMentorshipModalityException si el valor es inválido
     */
    public static MentorshipModality of(String modality) {
        return new MentorshipModality(modality);
    }

    /**
     * @return el valor normalizado de la modalidad
     */
    public String getValue() {
        return value;
    }

    /**
     * @return {@code true} si la modalidad es {@code REMOTE}
     */
    public boolean isRemote() {
        return REMOTE.equals(value);
    }

    /**
     * @return {@code true} si la modalidad es {@code IN_PERSON}
     */
    public boolean isInPerson() {
        return IN_PERSON.equals(value);
    }

    /**
     * @return {@code true} si la modalidad es {@code HYBRID}
     */
    public boolean isHybrid() {
        return HYBRID.equals(value);
    }
}
