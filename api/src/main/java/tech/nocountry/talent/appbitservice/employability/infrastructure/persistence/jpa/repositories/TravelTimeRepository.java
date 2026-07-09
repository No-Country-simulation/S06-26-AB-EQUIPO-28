package tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.TravelTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link TravelTime} aggregate root.
 *
 * <p>Provides derived query methods for the employability vertical's travel-time
 * queries (by origin/destination cluster) and is also a
 * {@link JpaSpecificationExecutor} to support dynamic filtering via
 * {@link tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications.TravelTimeSpecification}.</p>
 *
 * <p>The clusters are persisted as plain {@code String} columns
 * ({@code origin_cluster}, {@code destination_cluster}), so the derived query
 * methods use the field names directly without traversing embedded VOs.</p>
 */
@Repository
public interface TravelTimeRepository
        extends JpaRepository<TravelTime, UUID>, JpaSpecificationExecutor<TravelTime> {

    /**
     * Finds the travel-time record matching the given origin and destination clusters.
     *
     * @param origin the origin cluster name
     * @param dest   the destination cluster name
     * @return an optional containing the matching record, or empty if none exists
     */
    Optional<TravelTime> findByOriginClusterAndDestinationCluster(String origin, String dest);

    /**
     * Finds all travel-time records whose origin cluster matches the given name.
     *
     * @param origin the origin cluster name
     * @return list of matching travel-time records
     */
    List<TravelTime> findByOriginCluster(String origin);
}
