package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa la latitud geográfica.
 *
 * <p>Valida que la latitud esté en el rango [-90.0, 90.0].</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el valor de la latitud en grados decimales
 */
@Embeddable
public record Latitude(
        @Column(name = "latitude", nullable = false)  // CSV: lat
        Double value
) {
    private static final double MIN_LAT = -90.0;
    private static final double MAX_LAT = 90.0;

    public Latitude {
        if (value == null) {
            throw new IllegalArgumentException("La latitud no puede ser null");
        }
        if (value < MIN_LAT || value > MAX_LAT) {
            throw new IllegalArgumentException(
                    "La latitud debe estar entre " + MIN_LAT + " y " + MAX_LAT + ": " + value);
        }
    }

    @JsonCreator
    public static Latitude of(Double latitude) {
        return new Latitude(latitude);
    }

    @JsonValue
    public Double getValue() {
        return value;
    }
}
