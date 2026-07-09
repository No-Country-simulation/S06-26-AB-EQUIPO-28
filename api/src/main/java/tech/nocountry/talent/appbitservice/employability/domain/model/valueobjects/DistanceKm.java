package tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa una distancia en kilómetros (no negativa).
 *
 * <p>La validación garantiza que el valor sea {@code >= 0}. Se lanza
 * {@link IllegalArgumentException} para valores negativos, dado que es un error
 * de programación o de datos crudos, no de dominio de negocio.</p>
 */
@Embeddable
public record DistanceKm(
        @Column(name = "distance_km")
        double value
) {
    /** Constante estática que representa una distancia de cero kilómetros. */
    public static final DistanceKm ZERO = DistanceKm.of(0.0);

    /**
     * Constructor compact: valida que el valor no sea negativo.
     *
     * @throws IllegalArgumentException si {@code value < 0}
     */
    public DistanceKm {
        if (value < 0.0) {
            throw new IllegalArgumentException(
                    String.format("DistanceKm inválida: %f. Debe ser >= 0", value));
        }
    }

    /**
     * Factory que crea un {@link DistanceKm} a partir de un valor double.
     *
     * @param value distancia en kilómetros (>= 0)
     * @return nueva instancia de {@link DistanceKm}
     * @throws IllegalArgumentException si {@code value < 0}
     */
    public static DistanceKm of(double value) {
        return new DistanceKm(value);
    }

    /**
     * @return el valor de la distancia en kilómetros
     */
    public double getValue() {
        return value;
    }
}