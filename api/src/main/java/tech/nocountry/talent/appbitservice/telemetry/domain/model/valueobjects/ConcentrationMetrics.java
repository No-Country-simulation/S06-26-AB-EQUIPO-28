package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object que representa las métricas de calidad de red.
 *
 * <p>Contiene los indicadores de calidad de red como porcentaje de paquetes descartados
 * (drop) y nivel de congestión.</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * <p>Anteriormente conocido como ConcentracaoMetrics.</p>
 *
 * @param dropPct Porcentaje de paquetes descartados (0.0 a 1.0)
 * @param congestionLevel Nivel medio de congestión (0.0 a 1.0)
 */
@Embeddable
public record ConcentrationMetrics(
        @Column(name = "drop_pct")  // CSV: drop_pct_medio
        Double dropPct,

        @Column(name = "congestion_level")  // CSV: congestionamento_medio
        Double congestionLevel
) {
    private static final double MIN_VALUE = 0.0;
    private static final double MAX_VALUE = 1.0;

    public ConcentrationMetrics {
        if (dropPct != null && (dropPct < MIN_VALUE || dropPct > MAX_VALUE)) {
            throw new IllegalArgumentException("El porcentaje de drop debe estar entre 0.0 y 1.0");
        }
        if (congestionLevel != null && (congestionLevel < MIN_VALUE || congestionLevel > MAX_VALUE)) {
            throw new IllegalArgumentException("El nivel de congestión debe estar entre 0.0 y 1.0");
        }
    }

    /**
     * Crea una instancia de ConcentrationMetrics con los valores proporcionados.
     *
     * @param dropPct porcentaje de drop
     * @param congestionLevel nivel de congestión
     * @return nueva instancia
     */
    @JsonCreator
    public static ConcentrationMetrics of(Double dropPct, Double congestionLevel) {
        return new ConcentrationMetrics(dropPct, congestionLevel);
    }
}