package tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipFocusAreaException;

/**
 * Value Object que representa el área de enfoque de un programa de mentoría.
 *
 * <p>Dominio cerrado de valores permitidos:</p>
 * <ul>
 *   <li>{@code TECH} - Mentoría en tecnología / habilidades digitales</li>
 *   <li>{@code EMPLOYMENT} - Mentoría para empleabilidad</li>
 *   <li>{@code HEALTH} - Mentoría en salud (incluye salud mental)</li>
 *   <li>{@code CULTURE} - Mentoría cultural / experiencias estruturantes</li>
 *   <li>{@code EDUCATION} - Mentoría educativa / formación profesional</li>
 *   <li>{@code GENERAL} - Mentoría general (no específica)</li>
 * </ul>
 *
 * <p>La validación se realiza en el constructor compact del record: valores nulos,
 * vacíos o fuera del dominio lanzan {@link InvalidMentorshipFocusAreaException}.</p>
 *
 * <p>Sigue el patrón del {@code ConnectivityLevel} del bounded context de
 * inclusion-core: {@code @Embeddable record} con constantes estáticas, factory
 * {@code of(String)} case-insensitive y getters {@code isXxx()}.</p>
 */
@Embeddable
public record MentorshipFocusArea(
        @Column(name = "focus_area", nullable = false)
        String value
) {
    public static final String TECH = "TECH";
    public static final String EMPLOYMENT = "EMPLOYMENT";
    public static final String HEALTH = "HEALTH";
    public static final String CULTURE = "CULTURE";
    public static final String EDUCATION = "EDUCATION";
    public static final String GENERAL = "GENERAL";

    /**
     * Constructor compact: normaliza y valida el valor.
     *
     * @throws InvalidMentorshipFocusAreaException si el valor es nulo, vacío
     *         o no corresponde a ninguno de los valores permitidos.
     */
    public MentorshipFocusArea {
        if (value == null || value.isBlank()) {
            throw new InvalidMentorshipFocusAreaException(
                    "El área de enfoque de la mentoría no puede estar vacía"
            );
        }
        String normalized = value.toUpperCase().trim();
        if (!isValid(normalized)) {
            throw new InvalidMentorshipFocusAreaException(
                    String.format("Área de enfoque inválida: %s. Válidas: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL",
                            value)
            );
        }
        value = normalized;
    }

    private static boolean isValid(String normalized) {
        return TECH.equals(normalized)
                || EMPLOYMENT.equals(normalized)
                || HEALTH.equals(normalized)
                || CULTURE.equals(normalized)
                || EDUCATION.equals(normalized)
                || GENERAL.equals(normalized);
    }

    /**
     * Factory que crea un {@link MentorshipFocusArea} a partir de un string.
     *
     * <p>La normalización (trim + uppercase) es case-insensitive.</p>
     *
     * @param focusArea el área de enfoque (e.g., "tech", "EMPLOYMENT")
     * @return una nueva instancia de {@link MentorshipFocusArea}
     * @throws InvalidMentorshipFocusAreaException si el valor es inválido
     */
    public static MentorshipFocusArea of(String focusArea) {
        return new MentorshipFocusArea(focusArea);
    }

    /**
     * @return el valor normalizado del área de enfoque
     */
    public String getValue() {
        return value;
    }

    /**
     * @return {@code true} si el área es {@code TECH}
     */
    public boolean isTech() {
        return TECH.equals(value);
    }

    /**
     * @return {@code true} si el área es {@code EMPLOYMENT}
     */
    public boolean isEmployment() {
        return EMPLOYMENT.equals(value);
    }

    /**
     * @return {@code true} si el área es {@code HEALTH}
     */
    public boolean isHealth() {
        return HEALTH.equals(value);
    }

    /**
     * @return {@code true} si el área es {@code CULTURE}
     */
    public boolean isCulture() {
        return CULTURE.equals(value);
    }

    /**
     * @return {@code true} si el área es {@code EDUCATION}
     */
    public boolean isEducation() {
        return EDUCATION.equals(value);
    }

    /**
     * @return {@code true} si el área es {@code GENERAL}
     */
    public boolean isGeneral() {
        return GENERAL.equals(value);
    }
}
