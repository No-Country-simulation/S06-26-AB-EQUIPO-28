package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa el identificador único de una celda de antena (ECGI).
 *
 * <p>El ECGI (E-UTRAN Cell Global Identifier) proviene del dataset Vísent CDRView
 * y su longitud es variable según el registro. No se valida un largo fijo.</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el código ECGI (no nulo, no vacío)
 */
@Embeddable
public record Ecgi(
        @Column(name = "ecgi", nullable = false, length = 50)  // CSV: ecgi
        String value
) {
    public Ecgi {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El ECGI no puede ser nulo o estar en blanco");
        }
    }

    @JsonCreator
    public static Ecgi of(String ecgi) {
        return new Ecgi(ecgi);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}