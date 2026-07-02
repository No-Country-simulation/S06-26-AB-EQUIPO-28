package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object representing a concentration index (0-100).
 *
 * <p>Represents the concentration of people in a geographic region,
 * constrained to a valid range of 0 to 100.</p>
 */
@Embeddable
public record ConcentrationIndex(
        @Column(name = "value", nullable = false)
        double value
) {
    /**
     * Compact constructor for validation.
     *
     * @param value the concentration index
     * @throws IllegalArgumentException if value is outside 0-100 range
     */
    public ConcentrationIndex {
        if (value < 0.0 || value > 100.0) {
            throw new IllegalArgumentException("Concentration index must be between 0 and 100: " + value);
        }
    }

    /**
     * Creates a ConcentrationIndex with the given value.
     *
     * @param value the concentration index
     * @return new ConcentrationIndex
     */
    public static ConcentrationIndex of(double value) {
        return new ConcentrationIndex(value);
    }

    /** Zero concentration index. */
    public static ConcentrationIndex ZERO = new ConcentrationIndex(0.0);
}
