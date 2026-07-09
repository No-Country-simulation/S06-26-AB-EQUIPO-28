package tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.MobilityODPair;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for dynamic {@link MobilityODPair} queries.
 *
 * <p>These specifications compose with
 * {@link tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.MobilityODPairRepository}
 * to enable filtered searches from
 * {@link tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetMobilityODPairsQuery}.</p>
 *
 * <p>The {@code originCluster}, {@code destinationCluster} and
 * {@code predominantPeriod} attributes are persisted as plain {@code String}
 * columns (not embedded VOs), so they are accessed directly via
 * {@code root.get("fieldName")}.</p>
 */
public final class MobilityODPairSpecification {

    private MobilityODPairSpecification() {
    }

    /**
     * Filters mobility OD pairs by origin cluster.
     *
     * @param originCluster the origin cluster name (case-sensitive, must match the stored value)
     * @return a specification filtering by origin cluster
     */
    public static Specification<MobilityODPair> hasOriginCluster(String originCluster) {
        return (root, query, cb) -> {
            if (originCluster == null || originCluster.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("originCluster"), originCluster.trim());
        };
    }

    /**
     * Filters mobility OD pairs by destination cluster.
     *
     * @param destinationCluster the destination cluster name (case-sensitive, must match the stored value)
     * @return a specification filtering by destination cluster
     */
    public static Specification<MobilityODPair> hasDestinationCluster(String destinationCluster) {
        return (root, query, cb) -> {
            if (destinationCluster == null || destinationCluster.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("destinationCluster"), destinationCluster.trim());
        };
    }

    /**
     * Filters mobility OD pairs by predominant session period.
     *
     * <p>The {@code predominant_period} column stores the enum name of
     * {@link tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects.SessionPeriod}
     * (DAWN, MORNING, AFTERNOON, NIGHT) as a plain String. The filter is
     * case-insensitive and normalised to upper case before comparison.</p>
     *
     * @param predominantPeriod the period name (case-insensitive)
     * @return a specification filtering by predominant period
     */
    public static Specification<MobilityODPair> hasPredominantPeriod(String predominantPeriod) {
        return (root, query, cb) -> {
            if (predominantPeriod == null || predominantPeriod.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("predominantPeriod"), predominantPeriod.toUpperCase().trim());
        };
    }

    /**
     * Combines the optional origin, destination and period filters into a
     * single AND specification.
     *
     * <p>Null or blank parameters produce a conjunction (no filter) for that
     * dimension, so callers can safely pass partial filters.</p>
     *
     * @param originCluster      optional origin cluster filter
     * @param destinationCluster optional destination cluster filter
     * @param predominantPeriod  optional predominant period filter
     * @return a combined specification
     */
    public static Specification<MobilityODPair> buildFilter(
            String originCluster,
            String destinationCluster,
            String predominantPeriod
    ) {
        List<Specification<MobilityODPair>> specs = new ArrayList<>();
        specs.add(hasOriginCluster(originCluster));
        specs.add(hasDestinationCluster(destinationCluster));
        specs.add(hasPredominantPeriod(predominantPeriod));

        return specs.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}
