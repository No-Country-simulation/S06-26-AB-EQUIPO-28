package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración que representa el período de la sesión de red.
 *
 * <p>Divide el día en cuatro períodos para el análisis de tráfico de red:</p>
 * <ul>
 *   <li>DAWN - Horas 0 a 6</li>
 *   <li>MORNING - Horas 6 a 12</li>
 *   <li>AFTERNOON - Horas 12 a 18</li>
 *   <li>NIGHT - Horas 18 a 24</li>
 * </ul>
 *
 * <p>Anteriormente conocido como PeriodoSession (en portugués).</p>
 */
public enum SessionPeriod {
    DAWN,
    MORNING,
    AFTERNOON,
    NIGHT;

    /**
     * Convierte un valor de cadena a SessionPeriod.
     *
     * @param value el valor de cadena (case-insensitive)
     * @return el período correspondiente
     */
    @JsonCreator
    public static SessionPeriod from(String value) {
        if (value == null || value.isBlank()) { throw new IllegalArgumentException("El período no puede ser null o vacío"); }
        return SessionPeriod.valueOf(value.toUpperCase().trim());
    }

    /**
     * Obtiene el valor para serialización JSON.
     */
    @JsonValue
    public String toValue() {
        return name();
    }
}