package tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.employability.domain.exceptions.InvalidClusterException;

/**
 * Value Object que representa una referencia a un cluster geográfico funcional
 * de la Región Metropolitana de Florianópolis (CDRView).
 *
 * <p>El cluster es un identificador textual normalizado (e.g., {@code "CL_01"},
 * {@code "Centro"}). La validación garantiza que el valor no sea nulo ni vacío;
 * la pertenencia a un dominio cerrado no se força aquí porque los clusters son
 * especificados dinámicamente por el dataset CDRView.</p>
 *
 * <p>Sigue el patrón {@code @Embeddable record} con factory {@code of(String)}
 * y normalización (trim).</p>
 */
@Embeddable
public record ClusterRef(
        @Column(name = "cluster", nullable = false, length = 100)
        String value
) {
    /**
     * Constructor compact: valida que el valor no sea nulo ni vacío.
     *
     * @throws InvalidClusterException si el valor es nulo, vacío o solo espacios
     */
    public ClusterRef {
        if (value == null || value.isBlank()) {
            throw new InvalidClusterException(
                    value == null ? "null" : value);
        }
        value = value.trim();
    }

    /**
     * Factory que crea un {@link ClusterRef} a partir de un string.
     *
     * @param value referencia al cluster (se normaliza con trim)
     * @return nueva instancia de {@link ClusterRef}
     * @throws InvalidClusterException si el valor es nulo o vacío
     */
    public static ClusterRef of(String value) {
        return new ClusterRef(value);
    }

    /**
     * @return el valor normalizado de la referencia al cluster
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}