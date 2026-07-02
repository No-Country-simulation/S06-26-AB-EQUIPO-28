package tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;

import java.util.Optional;
import java.util.UUID;

/** Repositorio de roles. */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Roles name);
    boolean existsByName(Roles name);
}

