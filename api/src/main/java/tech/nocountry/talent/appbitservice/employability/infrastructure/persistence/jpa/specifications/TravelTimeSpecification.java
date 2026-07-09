package tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.TravelTime;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for dynamic {@link TravelTime} queries.
 *
 * <p>These specifications compose with
 * {@link tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.TravelTimeRepository}
 * to enable filtered searches from
 * {@link tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetTravelTimesQuery}.</p>
 *
 * <p>The {@code originCluster} and {@code destinationCluster} attributes are
 * persisted as plain {@code String} columns (not embedded VOs), so they are
 * accessed directly via {@code root.get("fieldName")}.</p>
 */
public final class TravelTimeSpecification {

    private TravelTimeSpecification() {
    }

    /**
     * Filters travel-time records by origin cluster.
     *
     * @param originCluster the origin cluster name (case-sensitive, must match the stored value)
     * @return a specification filtering by origin cluster
     */
    public static Specification<TravelTime> hasOriginCluster(String originCluster) {
        return (root, query, cb) -> {
            if (originCluster == null || originCluster.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("originCluster"), originCluster.trim());
        };
    }

    /**
     * Filters travel-time records by destination cluster.
     *
     * @param destinationCluster the destination cluster name (case-sensitive, must match the stored value)
     * @return a specification filtering by destination cluster
     */
    public static Specification<TravelTime> hasDestinationCluster(String destinationCluster) {
        return (root, query, cb) -> {
            if (destinationCluster == null || destinationCluster.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("destinationCluster"), destinationCluster.trim());
        };
    }

    /**
     * Combines the optional origin and destination filters into a single AND
     * specification.
     *
     * <p>Null or blank parameters produce a conjunction (no filter) for that
     * dimension, so callers can safely pass partial filters.</p>
     *
     * @param originCluster      optional origin cluster filter
     * @param destinationCluster optional destination cluster filter
     * @return a combined specification
     */
    public static Specification<TravelTime> buildFilter(
            String originCluster,
            String destinationCluster
    ) {
        List<Specification<TravelTime>> specs = new ArrayList<>();
        specs.add(hasOriginCluster(originCluster));
        specs.add(hasDestinationCluster(destinationCluster));

        return specs.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}
