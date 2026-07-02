package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions.InvalidConnectivityLevelException;

import java.util.Objects;

/**
 * Value Object representing a regional metric for social inclusion analysis.
 *
 * <p>Combines geographic information with vulnerability and connectivity metrics
 * to facilitate digital infrastructure gap analysis.</p>
 */
@Embeddable
public record RegionMetric(
        @Column(name = "region_name", nullable = false, length = 100)
        String regionName,

        @Embedded
        VulnerabilityScore vulnerabilityScore,

        @Embedded
        ConnectivityLevel connectivityLevel,

        @Column(name = "population_count")
        Integer populationCount,

        @Column(name = "average_drop_rate")
        Double averageDropRate,

        @Column(name = "congestion_level")
        Double congestionLevel
) {
    public RegionMetric {
        if (regionName == null || regionName.isBlank()) {
            throw new InvalidConnectivityLevelException("Region name cannot be empty");
        }
        Objects.requireNonNull(vulnerabilityScore, "vulnerabilityScore cannot be null");
        Objects.requireNonNull(connectivityLevel, "connectivityLevel cannot be null");
    }

    public static RegionMetric of(
            String regionName,
            VulnerabilityScore vulnerabilityScore,
            ConnectivityLevel connectivityLevel,
            Integer populationCount,
            Double averageDropRate
    ) {
        return new RegionMetric(
                regionName,
                vulnerabilityScore,
                connectivityLevel,
                populationCount,
                averageDropRate,
                null
        );
    }

    public static RegionMetric of(
            String regionName,
            VulnerabilityScore vulnerabilityScore,
            ConnectivityLevel connectivityLevel,
            Integer populationCount,
            Double averageDropRate,
            Double congestionLevel
    ) {
        return new RegionMetric(
                regionName,
                vulnerabilityScore,
                connectivityLevel,
                populationCount,
                averageDropRate,
                congestionLevel
        );
    }

    public boolean hasPoorInfrastructure() {
        return connectivityLevel.isLow();
    }

    public boolean isPriorityForIntervention() {
        boolean highVulnerability = vulnerabilityScore.getValue() > 60;
        boolean lowOrMediumConnectivity = connectivityLevel.isLow() || "MEDIUM".equals(connectivityLevel.getValue());
        return highVulnerability && lowOrMediumConnectivity;
    }
}
