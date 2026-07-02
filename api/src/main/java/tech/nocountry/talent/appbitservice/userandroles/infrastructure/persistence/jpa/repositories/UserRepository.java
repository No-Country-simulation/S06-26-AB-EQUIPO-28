package tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repositorio de usuarios. */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(UserName userName);

    boolean existsByUsername(UserName userName);

    @EntityGraph(attributePaths = {"roles"})
    List<User> findAllByIsActive(boolean isActive);

    /**
     * Returns all users with their roles eagerly loaded.
     * Uses EntityGraph to avoid N+1 queries when accessing roles.
     * Uses @Query to explicitly define the JPQL query and avoid Spring Data JPA
     * automatic query derivation issues with ManyToMany relationships.
     *
     * @return list of all users with roles
     */
    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM User u")
    List<User> findAllEager();

    /**
     * Returns user summaries with basic fields (userId, username, status).
     * Roles are intentionally excluded to avoid N+1 - they can be fetched separately if needed.
     * Uses a simple query that doesn't join the roles table.
     *
     * @return list of user id, username, status tuples
     */
    @Query("SELECT u.userId, u.username.value, u.isActive FROM User u")
    List<Object[]> findAllUserSummariesRaw();

    /**
     * Returns a user summary by ID.
     *
     * @param userId the user ID
     * @return optional tuple with user id, username, isActive
     */
    @Query("SELECT u.userId, u.username.value, u.isActive FROM User u WHERE u.userId = :userId")
    Optional<Object[]> findUserSummaryByIdRaw(@Param("userId") UUID userId);

    /**
     * Returns a user summary by username.
     *
     * @param username the username
     * @return optional tuple with user id, username, isActive
     */
    @Query("SELECT u.userId, u.username.value, u.isActive FROM User u WHERE u.username.value = :username")
    Optional<Object[]> findUserSummaryByUsernameRaw(@Param("username") String username);

    // Paginated methods with search

    /**
     * Returns all users paginated without search.
     * Uses EntityGraph to eagerly load roles.
     *
     * @param pageable pagination parameters
     * @return page of users
     */
    @EntityGraph(attributePaths = {"roles"})
    @NullMarked
    Page<User> findAll(Pageable pageable);

    /**
     * Returns all users paginated with search filter on userId, username, or roles.
     * Uses a custom JPQL query that joins roles for searching.
     *
     * @param search search string
     * @param pageable pagination parameters
     * @return page of users matching search criteria
     */
    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.roles r WHERE " +
           "LOWER(CAST(u.userId AS string)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.username.value) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> findAllWithSearch(@Param("search") String search, Pageable pageable);

    /**
     * Busca usuarios cuyo userId coincida exactamente con el UUID proporcionado.
     *
     * @param userId el UUID del usuario
     * @return lista de usuarios que coincidan con el userId
     */
    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    List<User> findByUserId(@Param("userId") UUID userId);

    /**
     * Busca usuarios cuyo username contenga el texto o tengan un rol que contenga el texto.
     * Utiliza busqueda insensible a mayusculas/minusculas.
     *
     * @param search texto a buscar en username o nombre de rol
     * @return lista de usuarios que coincidan con los criterios
     */
    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.roles r WHERE " +
           "LOWER(u.username.value) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> findByUsernameContainingOrRole(@Param("search") String search);
}