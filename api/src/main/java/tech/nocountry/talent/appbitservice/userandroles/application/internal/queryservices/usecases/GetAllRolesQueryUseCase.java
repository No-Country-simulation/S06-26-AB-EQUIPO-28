package tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetAllRolesQuery;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.List;

/**
 * Use case for retrieving all roles.
 */
@Service
@RequiredArgsConstructor
public class GetAllRolesQueryUseCase {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }
}
