package tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects;

import tech.nocountry.talent.appbitservice.employability.domain.exceptions.InvalidSessionPeriodException;

/**
 * Enum que representa los períodos de sesión ( franja horaria) bajo los cuales
 * CDRView agrega las observaciones de movilidad.
 *
 * <p>Es el lenguaje ubicuo del BC employability para clasificar el momento del
 * día en que predomina un patrón de viaje: {@code DAWN} (madrugada),
 * {@code MORNING} (mañana), {@code AFTERNOON} (tarde) y {@code NIGHT} (noche).
 * No es {@code @Embeddable} porque se persiste como {@code String} en las
 * tablas y se mapea trivialmente con {@code @Enumerated} o conversión manual.</p>
 *
 * <p>Los factories:</p>
 * <ul>
 *   <li>{@link #fromPortuguese(String)} traduce los labels del CSV original
 *       (portugués) al enum.</li>
 *   <li>{@link #fromString(String)} acepta el nombre del enum (case-insensitive).</li>
 * </ul>
 * <p>Ambos factories regresan {@code NIGHT} por defecto cuando el input es
 * nulo o vacío (lanzan solo ante un valor presente pero inválido).</p>
 */
public enum SessionPeriod {
    DAWN, MORNING, AFTERNOON, NIGHT;

    /**
     * Traduce los labels en portugués del CSV CDRView al enum.
     *
     * <p>Soporta {@code MADRUGADA}, {@code MANHA}, {@code MANHÃ}, {@code TARDE}
     * y {@code NOITE}. Un input nulo o vacío se interpreta como {@code NIGHT}
     * (valor por defecto del dataset).</p>
     *
     * @param pt label en portugués (e.g., "MADRUGADA")
     * @return el {@link SessionPeriod} correspondiente
     * @throws InvalidSessionPeriodException si el valor es presente pero no reconocido
     */
    public static SessionPeriod fromPortuguese(String pt) {
        if (pt == null || pt.isBlank()) return NIGHT; // default
        return switch (pt.toUpperCase().trim()) {
            case "MADRUGADA" -> DAWN;
            case "MANHA", "MANHÃ" -> MORNING;
            case "TARDE" -> AFTERNOON;
            case "NOITE" -> NIGHT;
            default -> throw new InvalidSessionPeriodException(pt);
        };
    }

    /**
     * Acepta el nombre del enum en cualquier capitalización (case-insensitive).
     *
     * @param value nombre del enum (e.g., "dawn", "MORNING")
     * @return el {@link SessionPeriod} correspondiente
     * @throws InvalidSessionPeriodException si el valor es presente pero no reconocido
     */
    public static SessionPeriod fromString(String value) {
        if (value == null || value.isBlank()) return NIGHT;
        try {
            return SessionPeriod.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidSessionPeriodException(value);
        }
    }
}