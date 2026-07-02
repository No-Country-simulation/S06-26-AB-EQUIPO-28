package tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Set;

/**
 * Patrón de movilidad del suscriptor.
 * Proviene del campo {@code mobility_pattern} del CSV de suscriptores.
 * Indica el comportamiento de desplazamiento del ciudadano:
 * LOW - baja movilidad (permanece cerca de su hogar)
 * MODERATE - movilidad moderada
 * INTENSE - alta movilidad (se desplaza frecuentemente entre clusters)
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el patrón de movilidad
 */
@Embeddable
public record MobilityPattern(
        @Column(name = "mobility_pattern", nullable = false, length = 10)  // CSV: mobility_pattern
        String value
) {
    private static final Set<String> VALID_PATTERNS = Set.of("LOW", "MODERATE", "INTENSE");

    public MobilityPattern {
        if (value == null) { throw new IllegalArgumentException("El patron de movilidad no puede ser null"); }
        String upperValue = value.toUpperCase().trim();

        if (upperValue.isBlank()) { throw new IllegalArgumentException("El patron de movilidad no puede estar en blanco"); }
        if (!VALID_PATTERNS.contains(upperValue)) {
            throw new IllegalArgumentException("El patron de movilidad debe ser uno de: LOW, MODERATE, INTENSE");
        }
        value = upperValue;
    }

    @JsonCreator
    public static MobilityPattern of(String value) {
        return new MobilityPattern(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}