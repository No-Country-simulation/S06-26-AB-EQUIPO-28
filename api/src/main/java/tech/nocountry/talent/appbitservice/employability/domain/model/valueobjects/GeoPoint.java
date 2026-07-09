package tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa un punto geográfico (latitud, longitud) en el
 * sistema de coordenadas WGS84.
 *
 * <p>La validación garantiza que la latitud esté en {@code [-90, 90]} y la
 * longitud en {@code [-180, 180]}. Se lanza {@link IllegalArgumentException}
 * para los valores fuera de rango, dado que es un error de programación (no
 * de dominio de negocio).</p>
 */
@Embeddable
public record GeoPoint(
        @Column(name = "latitude")
        double latitude,
        @Column(name = "longitude")
        double longitude
) {
    /**
     * Constructor compact: valida los rangos de latitud y longitud.
     *
     * @throws IllegalArgumentException si la latitud o longitud están fuera de rango
     */
    public GeoPoint {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException(
                    String.format("Latitude inválida: %f. Debe estar entre -90 y 90", latitude));
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException(
                    String.format("Longitude inválida: %f. Debe estar entre -180 y 180", longitude));
        }
    }

    /**
     * Factory que crea un {@link GeoPoint} a partir de sus componentes.
     *
     * @param latitude  latitud en grados decimales [-90, 90]
     * @param longitude longitud en grados decimales [-180, 180]
     * @return nueva instancia de {@link GeoPoint}
     * @throws IllegalArgumentException si los valores están fuera de rango
     */
    public static GeoPoint of(double latitude, double longitude) {
        return new GeoPoint(latitude, longitude);
    }

    /**
     * @return la latitud en grados decimales
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return la longitud en grados decimales
     */
    public double getLongitude() {
        return longitude;
    }
}