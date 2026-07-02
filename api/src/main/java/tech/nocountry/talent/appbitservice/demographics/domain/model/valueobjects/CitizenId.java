package tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Identificador único del suscriptor anonimizado.
 * Proviene del campo {@code assinante_hash} del CSV de suscriptores.
 * Es el identificador interno que permite cruzar datos del ciudadano
 * sin exponer datos personales (anonimización).
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el hash del ciudadano
 */
@Embeddable
public record CitizenId(
        @Column(name = "citizen_hash", nullable = false, length = 64)  // CSV: assinante_hash
        String value
) {
    public CitizenId {
        if (value == null) { throw new IllegalArgumentException("El ID del ciudadano no puede ser null"); }
        if (value.isBlank()) { throw new IllegalArgumentException("El ID del ciudadano no puede estar en blanco"); }
        if (value.length() < 1 || value.length() > 64) {
            throw new IllegalArgumentException( "El ID del ciudadano debe tener entre 1 y 64 caracteres");
        }
    }

    @JsonCreator
    public static CitizenId of(String value) {
        return new CitizenId(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}