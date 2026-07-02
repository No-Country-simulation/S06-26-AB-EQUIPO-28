package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a population count.
 *
 * <p>Ensures that population counts are never negative.
 * Wraps an integer value and provides factory methods for creation.</p>
 */
@Embeddable
public record PopulationCount(
        @Column(name = "value", nullable = false)
        int value
) {
    /**
     * Compact constructor for validation.
     *
     * @param value the population count
     * @throws IllegalArgumentException if value is negative
     */
    public PopulationCount {
        if (value < 0) {
            throw new IllegalArgumentException("Population count cannot be negative: " + value);
        }
    }

    /**
     * Creates a PopulationCount with the given value.
     *
     * @param value the population count
     * @return new PopulationCount
     */
    public static PopulationCount of(int value) {
        return new PopulationCount(value);
    }

    /** Zero population count. */
    public static PopulationCount ZERO = new PopulationCount(0);
}
