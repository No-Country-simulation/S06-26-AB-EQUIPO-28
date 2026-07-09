package tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipTargetAudienceException;

/**
 * Value Object que representa el público objetivo de un programa de mentoría.
 *
 * <p>Dominio cerrado de valores permitidos:</p>
 * <ul>
 *   <li>{@code YOUNG_ADULTS} - Jóvenes adultos</li>
 *   <li>{@code WOMEN} - Mujeres</li>
 *   <li>{@code ELDERLY} - Adultos mayores</li>
 *   <li>{@code GENERAL} - Público general (sin segmentación demográfica)</li>
 * </ul>
 *
 * <p><b>Nullabilidad</b>: a diferencia de {@link MentorshipFocusArea} y
 * {@link MentorshipModality}, el {@code target_audience} es una columna nullable
 * en la base de datos (un programa puede no segmentar por demografía). Por eso,
 * el factory {@link #of(String)} <b>acepta {@code null}</b> y devuelve
 * {@code null}, y la validación solo se aplica cuando el valor no es {@code null}.</p>
 *
 * <p>Sigue el patrón del {@code ConnectivityLevel} del bounded context de
 * inclusion-core, con la adaptación de nullabilidad.</p>
 */
@Embeddable
public record MentorshipTargetAudience(
        @Column(name = "target_audience")
        String value
) {
    public static final String YOUNG_ADULTS = "YOUNG_ADULTS";
    public static final String WOMEN = "WOMEN";
    public static final String ELDERLY = "ELDERLY";
    public static final String GENERAL = "GENERAL";

    /**
     * Constructor compact: si el valor es {@code null} o blank, lo normaliza a
     * {@code null} (público no segmentado es válido). Si no es blank, valida contra
     * el dominio cerrado.
     *
     * @throws InvalidMentorshipTargetAudienceException si el valor no es blank
     *         pero tampoco corresponde a ninguno de los valores permitidos.
     */
    public MentorshipTargetAudience {
        if (value != null) {
            String trimmed = value.trim();
            if (trimmed.isEmpty()) {
                value = null;
            } else {
                String normalized = trimmed.toUpperCase();
                if (!isValid(normalized)) {
                    throw new InvalidMentorshipTargetAudienceException(
                            String.format("Público objetivo inválido: %s. Válidos: YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL", value)
                    );
                }
                value = normalized;
            }
        }
    }

    private static boolean isValid(String normalized) {
        return YOUNG_ADULTS.equals(normalized)
                || WOMEN.equals(normalized)
                || ELDERLY.equals(normalized)
                || GENERAL.equals(normalized);
    }

    /**
     * Factory que crea un {@link MentorshipTargetAudience} a partir de un string.
     *
     * <p><b>Acepta {@code null}</b>: devuelve {@code null} en ese caso, porque el
     * público objetivo es opcional (columna nullable). La validación contra el
     * dominio cerrado solo aplica cuando el valor no es {@code null} ni blank.</p>
     *
     * @param targetAudience el público objetivo (e.g., "women", "ELDERLY"), o {@code null}
     * @return una nueva instancia, o {@code null} si el input es {@code null}/blank
     * @throws InvalidMentorshipTargetAudienceException si el valor no es blank
     *         pero es inválido
     */
    public static MentorshipTargetAudience of(String targetAudience) {
        if (targetAudience == null) {
            return null;
        }
        String trimmed = targetAudience.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return new MentorshipTargetAudience(targetAudience);
    }

    /**
     * @return el valor normalizado del público objetivo, o {@code null}
     */
    public String getValue() {
        return value;
    }

    /**
     * @return {@code true} si el público es {@code YOUNG_ADULTS}
     */
    public boolean isYoungAdults() {
        return YOUNG_ADULTS.equals(value);
    }

    /**
     * @return {@code true} si el público es {@code WOMEN}
     */
    public boolean isWomen() {
        return WOMEN.equals(value);
    }

    /**
     * @return {@code true} si el público es {@code ELDERLY}
     */
    public boolean isElderly() {
        return ELDERLY.equals(value);
    }

    /**
     * @return {@code true} si el público es {@code GENERAL}
     */
    public boolean isGeneral() {
        return GENERAL.equals(value);
    }
}
