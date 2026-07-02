package tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Nivel socioeconómico del suscriptor.
 * Proviene del campo {@code income_level} del CSV de suscriptores.
 * Representa la clasificación de renta del ciudadano (A: alta, B: media-alta, C: media-baja, D: baja).
 * Se utiliza para identificar poblaciones vulnerables para políticas de inclusión social.
 *
 * <p>El nivel D es especialmente importante para el análisis de inclusión social.</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el nivel de ingreso (A, B, C o D)
 */
@Embeddable
public record IncomeLevel(
        @Column(name = "income_level", nullable = false, length = 1)  // CSV: income_level
        String value
) {
    private static final String VALID_LEVELS = "ABCD";

    public IncomeLevel {
        if (value == null) { throw new IllegalArgumentException("El nivel de ingreso no puede ser null"); }
        String upperValue = value.toUpperCase().trim();

        if (upperValue.isBlank()) { throw new IllegalArgumentException("El nivel de ingreso no puede estar en blanco"); }
        if (upperValue.length() != 1 || !VALID_LEVELS.contains(upperValue)) {
            throw new IllegalArgumentException("El nivel de ingreso debe ser uno de: A, B, C, D");
        }
        value = upperValue;
    }

    @JsonCreator
    public static IncomeLevel of(String value) {
        return new IncomeLevel(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Verifica si este nivel representa población vulnerable (baja renta).
     *
     * @return true si el nivel es D
     */
    public boolean isVulnerable() {
        return "D".equals(value);
    }
}
