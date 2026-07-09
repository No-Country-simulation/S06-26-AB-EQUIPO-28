package tech.nocountry.talent.appbitservice.employability.domain.services;

import tech.nocountry.talent.appbitservice.employability.domain.model.queries.EmployabilityGap;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GapSeverity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de dominio que calcula la {@link EmployabilityGap} de un cluster
 * geográfico cruzando señales de telemetría, demografía y movilidad.
 *
 * <p>El algoritmo de scoring suma señales ponderadas y empaqueta los factores
 * que dispararon el cálculo para trazabilidad explicativa. Es una clase de
 * utilidad estática (no Spring bean) porque es pura y sin dependencias: el
 * query service que la invoca es responsable de proveer los umbrales de
 * cuartiles calculados dinámicamente sobre el universo de clusters.</p>
 *
 * <p><b>Modelo de scoring (gapScore = suma de señales)</b>:</p>
 * <ul>
 *   <li>{@code +30} si {@code !hasTelemetryCoverage} (zona ciega de telemetría).</li>
 *   <li>{@code +20} si {@code daytimeAvgUsers} está en el cuartil inferior
 *       ({@code < daytimeQ1Threshold}).</li>
 *   <li>{@code +15} si la proporción de renta baja
 *       {@code (incomeC + incomeD) / citizenCount > 0.60}.</li>
 *   <li>{@code +15} si {@code distanceToNearestHubKm > 15.0}.</li>
 *   <li>{@code +10} si {@code outboundTripsToHubs} está en el cuartil inferior
 *       ({@code < outboundQ1Threshold}).</li>
 *   <li>{@code +10} si la proporción de jóvenes
 *       {@code (double) youthCount18_24 / citizenCount > 0.22}.</li>
 * </ul>
 *
 * <p>El puntaje se acota en {@code [0, 100]} y la severidad se deriva con
 * {@link GapSeverity#fromScore(double)}.</p>
 */
public final class EmployabilityGapCalculator {

    /** Umbral fijo de proporción de renta baja (C + D) sobre el total de ciudadanos. */
    private static final double LOW_INCOME_RATIO_THRESHOLD = 0.60;
    /** Umbral fijo de distancia al hub de empleo más cercano (km). */
    private static final double DISTANCE_TO_HUB_THRESHOLD_KM = 15.0;
    /** Umbral fijo de proporción de jóvenes (18-24) sobre el total de ciudadanos. */
    private static final double YOUTH_RATIO_THRESHOLD = 0.22;
    /** Puntaje máximo admisible (acota la suma de señales). */
    private static final double MAX_SCORE = 100.0;
    private static final double MIN_SCORE = 0.0;

    private EmployabilityGapCalculator() {
        // utility class, no instanciable
    }

    /**
     * Calcula la brecha de empleabilidad de un cluster a partir de las señales
     * crudas y los umbrales de cuartiles provistos por el query service.
     *
     * @param cluster                    nombre del cluster
     * @param municipalities             municipios que componen el cluster
     * @param citizenCount               total de ciudadanos (debe ser > 0 para
     *                                   los ratios; si es 0 los ratios se tratan como 0)
     * @param incomeDCount               ciudadanos en renta D
     * @param incomeCCount               ciudadanos en renta C
     * @param youthCount18_24            jóvenes de 18-24 años
     * @param hasTelemetryCoverage       {@code true} si hay cobertura de telemetría
     * @param daytimeAvgUsers            promedio de usuarios en horario laboral
     * @param outboundTripsToHubs        viajes salientes hacia hubs de empleo
     * @param distanceToNearestHubKm     distancia en km al hub más cercano
     * @param mobilityIntensity          etiqueta descriptiva de intensidad de movilidad
     * @param daytimeQ1Threshold         umbral del cuartil inferior de densidad diurna
     * @param outboundQ1Threshold        umbral del cuartil inferior de viajes salientes
     * @return una {@link EmployabilityGap} con score, severity y primaryFactors
     */
    public static EmployabilityGap calculate(
            String cluster,
            List<String> municipalities,
            long citizenCount,
            long incomeDCount,
            long incomeCCount,
            long youthCount18_24,
            boolean hasTelemetryCoverage,
            double daytimeAvgUsers,
            long outboundTripsToHubs,
            double distanceToNearestHubKm,
            String mobilityIntensity,
            double daytimeQ1Threshold,
            double outboundQ1Threshold) {
        Objects.requireNonNull(cluster, "cluster no puede ser null");
        List<String> factors = new ArrayList<>();
        double score = 0.0;

        // Señal 1: cobertura de telemetría (+30 si no hay cobertura)
        if (!hasTelemetryCoverage) {
            score += 30.0;
            factors.add("No telemetry coverage (+30)");
        }

        // Señal 2: densidad diurna en el cuartil inferior (+20)
        if (daytimeAvgUsers < daytimeQ1Threshold) {
            score += 20.0;
            factors.add(String.format("Low daytime density (%.0f < %.0f) (+20)",
                    daytimeAvgUsers, daytimeQ1Threshold));
        }

        // Señal 3: proporción de renta baja (C + D) sobre ciudadanos (+15)
        double lowIncomeRatio = citizenCount > 0
                ? (double) (incomeCCount + incomeDCount) / citizenCount
                : 0.0;
        if (lowIncomeRatio > LOW_INCOME_RATIO_THRESHOLD) {
            score += 15.0;
            factors.add(String.format("High low-income ratio (%.2f > %.2f) (+15)",
                    lowIncomeRatio, LOW_INCOME_RATIO_THRESHOLD));
        }

        // Señal 4: distancia al hub de empleo más cercano (+15)
        if (distanceToNearestHubKm > DISTANCE_TO_HUB_THRESHOLD_KM) {
            score += 15.0;
            factors.add(String.format("Far from employment hub (%.1f km > %.1f km) (+15)",
                    distanceToNearestHubKm, DISTANCE_TO_HUB_THRESHOLD_KM));
        }

        // Señal 5: viajes salientes en el cuartil inferior (+10)
        if (outboundTripsToHubs < outboundQ1Threshold) {
            score += 10.0;
            factors.add(String.format("Low outbound mobility (%d < %.0f) (+10)",
                    outboundTripsToHubs, outboundQ1Threshold));
        }

        // Señal 6: proporción de jóvenes (18-24) (+10)
        double youthRatio = citizenCount > 0
                ? (double) youthCount18_24 / citizenCount
                : 0.0;
        if (youthRatio > YOUTH_RATIO_THRESHOLD) {
            score += 10.0;
            factors.add(String.format("High youth ratio (%.2f > %.2f) (+10)",
                    youthRatio, YOUTH_RATIO_THRESHOLD));
        }

        // Acota el score en [0, 100] y deriva la severidad.
        double finalScore = Math.max(MIN_SCORE, Math.min(MAX_SCORE, score));
        GapSeverity severity = GapSeverity.fromScore(finalScore);

        return new EmployabilityGap(
                cluster,
                municipalities,
                citizenCount,
                incomeDCount,
                incomeCCount,
                youthCount18_24,
                hasTelemetryCoverage,
                daytimeAvgUsers,
                outboundTripsToHubs,
                distanceToNearestHubKm,
                mobilityIntensity,
                severity.name(),
                finalScore,
                List.copyOf(factors)
        );
    }
}