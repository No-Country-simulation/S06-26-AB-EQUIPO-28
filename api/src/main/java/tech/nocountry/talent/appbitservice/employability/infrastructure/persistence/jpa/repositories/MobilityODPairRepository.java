package tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.MobilityODPair;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link MobilityODPair} aggregate root.
 *
 * <p>Provides derived query methods for the employability vertical's mobility
 * queries (by origin/destination cluster) and is also a
 * {@link JpaSpecificationExecutor} to support dynamic filtering via
 * {@link tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications.MobilityODPairSpecification}.</p>
 *
 * <p>The clusters are persisted as plain {@code String} columns
 * ({@code origin_cluster}, {@code destination_cluster}), so the derived query
 * methods use the field names directly without traversing embedded VOs.</p>
 */
@Repository
public interface MobilityODPairRepository
        extends JpaRepository<MobilityODPair, UUID>, JpaSpecificationExecutor<MobilityODPair> {

    /**
     * Finds the mobility OD pair matching the given origin and destination clusters.
     *
     * @param origin the origin cluster name
     * @param dest   the destination cluster name
     * @return an optional containing the matching pair, or empty if none exists
     */
    Optional<MobilityODPair> findByOriginClusterAndDestinationCluster(String origin, String dest);

    /**
     * Finds all mobility OD pairs whose origin cluster matches the given name.
     *
     * @param origin the origin cluster name
     * @return list of matching OD pairs
     */
    List<MobilityODPair> findByOriginCluster(String origin);

    /**
     * Finds all mobility OD pairs whose destination cluster matches the given name.
     *
     * @param dest the destination cluster name
     * @return list of matching OD pairs
     */
    List<MobilityODPair> findByDestinationCluster(String dest);
}
