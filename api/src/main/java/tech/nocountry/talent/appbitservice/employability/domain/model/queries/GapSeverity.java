package tech.nocountry.talent.appbitservice.employability.domain.model.queries;

/**
 * Enum que representa la severidad de una brecha de empleabilidad, derivada
 * del {@code gapScore} calculado por {@link
 * tech.nocountry.talent.appbitservice.employability.domain.services.EmployabilityGapCalculator}.
 *
 * <p>La clasificación por umbrales es:</p>
 * <ul>
 *   <li>{@code CRITICAL} para {@code score >= 70}</li>
 *   <li>{@code HIGH} para {@code 50 <= score < 70}</li>
 *   <li>{@code MODERATE} para {@code 30 <= score < 50}</li>
 *   <li>{@code LOW} para {@code 15 <= score < 30}</li>
 *   <li>{@code NONE} para {@code score < 15}</li>
 * </ul>
 */
public enum GapSeverity {
    CRITICAL, HIGH, MODERATE, LOW, NONE;

    /**
     * Deriva la severidad a partir del puntaje agregado.
     *
     * @param score puntaje de la brecha (0-100)
     * @return la {@link GapSeverity} correspondiente
     */
    public static GapSeverity fromScore(double score) {
        if (score >= 70) return CRITICAL;
        if (score >= 50) return HIGH;
        if (score >= 30) return MODERATE;
        if (score >= 15) return LOW;
        return NONE;
    }
}