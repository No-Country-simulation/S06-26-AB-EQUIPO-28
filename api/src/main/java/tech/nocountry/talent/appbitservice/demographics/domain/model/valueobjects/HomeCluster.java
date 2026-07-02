package tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Zona geográfica de residencia del suscriptor.
 * Proviene del campo {@code home_cluster} del CSV de suscriptores.
 * Agrupa áreas funcionales de la Región Metropolitana de Florianópolis
 * para análisis de concentración poblacional.
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el nombre del cluster de residencia
 */
@Embeddable
public record HomeCluster(
        @Column(name = "home_cluster", nullable = false, length = 50)  // CSV: home_cluster
        String value
) {
    public HomeCluster {
        if (value == null) { throw new IllegalArgumentException("El cluster de residencia no puede ser null"); }
        if (value.isBlank()) { throw new IllegalArgumentException("El cluster de residencia no puede estar en blanco"); }
        if (value.length() > 50) {
            throw new IllegalArgumentException("El cluster de residencia no puede exceder 50 caracteres");
        }
    }

    @JsonCreator
    public static HomeCluster of(String value) {
        return new HomeCluster(value.trim());
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}