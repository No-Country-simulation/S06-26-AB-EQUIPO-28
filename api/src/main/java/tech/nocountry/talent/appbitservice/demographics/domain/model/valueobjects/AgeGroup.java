package tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Rango etario del suscriptor.
 * Proviene del campo {@code age_group} del CSV de suscriptores.
 * Valores esperados: 18-24, 25-34, 35-44, 45-54, 55+.
 * Se utiliza para segmentar el tipo de soporte social necesario.
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el grupo de edad
 */
@Embeddable
public record AgeGroup(
        @Column(name = "age_group", nullable = false, length = 10)  // CSV: age_group
        String value
) {
    private static final String[] VALID_GROUPS = {"18-24", "25-34", "35-44", "45-54", "55+"};

    public AgeGroup {
        if (value == null) { throw new IllegalArgumentException("El grupo de edad no puede ser null"); }
        String trimmedValue = value.trim();

        if (trimmedValue.isBlank()) { throw new IllegalArgumentException("El grupo de edad no puede estar en blanco"); }
        boolean valid = false;

        for (String group : VALID_GROUPS) {
            if (group.equals(trimmedValue)) {
                valid = true;
                break;
            }
        }

        if (!valid) { throw new IllegalArgumentException("El grupo de edad debe ser uno de: 18-24, 25-34, 35-44, 45-54, 55+"); }
        value = trimmedValue;
    }

    @JsonCreator
    public static AgeGroup of(String value) {
        return new AgeGroup(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}