package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa la longitud geográfica.
 *
 * <p>Valida que la longitud esté en el rango [-180.0, 180.0].</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el valor de la longitud en grados decimales
 */
@Embeddable
public record Longitude(
        @Column(name = "longitude", nullable = false)  // CSV: lon
        Double value
) {
    private static final double MIN_LON = -180.0;
    private static final double MAX_LON = 180.0;

    public Longitude {
        if (value == null) {
            throw new IllegalArgumentException("La longitud no puede ser null");
        }
        if (value < MIN_LON || value > MAX_LON) {
            throw new IllegalArgumentException(
                    "La longitud debe estar entre " + MIN_LON + " y " + MAX_LON + ": " + value);
        }
    }

    @JsonCreator
    public static Longitude of(Double longitude) {
        return new Longitude(longitude);
    }

    @JsonValue
    public Double getValue() {
        return value;
    }
}
