package tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link MentorshipProgram} aggregate root.
 *
 * <p>Provides query methods for the mentorship vertical's Phase 2
 * (JPA Repository + Specification + ACL to inclusioncore).</p>
 */
@Repository
public interface MentorshipProgramRepository extends JpaRepository<MentorshipProgram, UUID>, JpaSpecificationExecutor<MentorshipProgram> {

    /**
     * Finds a mentorship program by its business identifier (program_id column).
     *
     * @param programId the business program ID
     * @return optional containing the program if found
     */
    Optional<MentorshipProgram> findByProgramId(String programId);

    /**
     * Finds all active mentorship programs.
     *
     * @return list of active programs
     */
    List<MentorshipProgram> findByIsActiveTrue();

    /**
     * Finds all mentorship programs associated with a specific geographic cluster.
     *
     * @param clusterName the cluster name
     * @return list of programs in that cluster
     */
    List<MentorshipProgram> findByClusterName(String clusterName);

    /**
     * Finds all active mentorship programs associated with a specific geographic cluster.
     *
     * @param clusterName the cluster name
     * @return list of active programs in that cluster
     */
    List<MentorshipProgram> findByClusterNameAndIsActiveTrue(String clusterName);

    /**
     * Checks whether a mentorship program with the given business ID exists.
     *
     * @param programId the business program ID
     * @return {@code true} if a program with that ID exists
     */
    boolean existsByProgramId(String programId);
}