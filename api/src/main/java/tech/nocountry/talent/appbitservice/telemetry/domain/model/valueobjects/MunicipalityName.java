package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa el nombre de un municipio.
 *
 * <p>Identifica el municipio al que pertenece una antenna o concentración de usuarios.</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el nombre del municipio
 */
@Embeddable
public record MunicipalityName(
        @Column(name = "municipality", nullable = false, length = 100)
        String value
) {
    public MunicipalityName {
        if (value == null) { throw new IllegalArgumentException("El nombre del municipio no puede ser null"); }
        if (value.isBlank()) { throw new IllegalArgumentException("El nombre del municipio no puede estar en blanco"); }
    }

    /**
     * Crea una instancia de MunicipalityName con el valor proporcionado.
     *
     * @param municipalityName el nombre del municipio
     * @return nueva instancia
     */
    @JsonCreator
    public static MunicipalityName of(String municipalityName) {
        return new MunicipalityName(municipalityName);
    }

    /**
     * Obtiene el valor para serialización JSON.
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}