package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions.InvalidConnectivityLevelException;

/**
 * Value Object representing the network connectivity level.
 *
 * <p>Determined based on the percentage of dropped packets (drop percentage).
 * This level is critical for evaluating telemedicine and remote education viability.</p>
 */
@Embeddable
public record ConnectivityLevel(
        @Column(name = "connectivity_level", nullable = false)
        String value
) {
    public static final ConnectivityLevel HIGH = new ConnectivityLevel("HIGH");
    public static final ConnectivityLevel MEDIUM = new ConnectivityLevel("MEDIUM");
    public static final ConnectivityLevel LOW = new ConnectivityLevel("LOW");

    private static final String HIGH_VALUE = "HIGH";
    private static final String MEDIUM_VALUE = "MEDIUM";
    private static final String LOW_VALUE = "LOW";

    public ConnectivityLevel {
        if (value == null || value.isBlank()) {
            throw new InvalidConnectivityLevelException("Connectivity level cannot be empty");
        }
        value = value.toUpperCase().trim();
        if (!value.equals(HIGH_VALUE) && !value.equals(MEDIUM_VALUE) && !value.equals(LOW_VALUE)) {
            throw new InvalidConnectivityLevelException(
                    String.format("Invalid connectivity level: %s. Must be HIGH, MEDIUM or LOW", value)
            );
        }
    }

    /**
     * Creates a ConnectivityLevel from a string.
     *
     * @param level the connectivity level ("HIGH", "MEDIUM" or "LOW")
     * @return a new ConnectivityLevel instance
     */
    public static ConnectivityLevel of(String level) {
        return new ConnectivityLevel(level);
    }

    /**
     * Creates a ConnectivityLevel from drop percentage.
     *
     * <p>The {@code drop_pct} telemetry column is stored as a fraction in the
     * range [0.0-1.0] (per the CDRView technical reference), where 0.069 means
     * a 6.9% packet drop rate. Thresholds are expressed in that same fraction.</p>
     *
     * <p>Classification logic (recalibrated):</p>
     * <ul>
     *   <li>Drop &lt; 0.0686: HIGH</li>
     *   <li>Drop 0.0686-0.0687: MEDIUM</li>
     *   <li>Drop &gt;= 0.0687: LOW</li>
     * </ul>
     *
     * <p><b>Recalibration note:</b> The original thresholds (0.02 / 0.05) were
     * calibrated for real-world telecom telemetry, where drop rates span a wide
     * range. The synthetic hackathon dataset CDRView (Regi&atilde;o Metropolitana
     * de Florian&oacute;polis, 23 clusters) has {@code avg_drop_pct} values
     * concentrated in the narrow band 0.0684&ndash;0.0688 (range 0.0004), so the
     * original thresholds failed to discriminate: virtually every cluster fell
     * into the LOW bucket. The current thresholds are the P33 (&asymp; 0.068555)
     * and P66 (&asymp; 0.068632) percentiles computed over the 23 real clusters,
     * rounded to 4 decimal places for readability. This splits the population
     * into three roughly equal terciles by connectivity quality (lower drop
     * rate &rarr; better connectivity &rarr; HIGH).</p>
     *
     * @param dropPct the fraction of dropped packets [0.0-1.0]; {@code null} maps to LOW
     * @return the corresponding connectivity level
     */
    public static ConnectivityLevel fromDropPercentage(Double dropPct) {
        if (dropPct == null) {
            return LOW;
        }
        if (dropPct < 0.0686) {
            return HIGH;
        } else if (dropPct < 0.0687) {
            return MEDIUM;
        } else {
            return LOW;
        }
    }

    /**
     * Returns the connectivity level value.
     *
     * @return "HIGH", "MEDIUM" or "LOW"
     */
    public String getValue() {
        return value;
    }

    /**
     * Checks if the level is HIGH.
     *
     * @return true if the level is HIGH
     */
    public boolean isHigh() {
        return HIGH_VALUE.equals(value);
    }

    /**
     * Checks if the level is LOW.
     *
     * @return true if the level is LOW
     */
    public boolean isLow() {
        return LOW_VALUE.equals(value);
    }

    /**
     * Compares connectivity levels.
     *
     * @param other the other ConnectivityLevel to compare
     * @return true if this level has higher priority (HIGH &gt; MEDIUM &gt; LOW)
     */
    public boolean hasHigherPriorityThan(ConnectivityLevel other) {
        return this.equals(HIGH) || (this.equals(MEDIUM) && other.equals(LOW));
    }
}