package tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SeedRolesCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.Arrays;

/**
 * Use case for seeding initial system roles.
 */
@Service
@RequiredArgsConstructor
public class SeedRolesCommandUseCase {
    private final RoleRepository roleRepository;

    @Transactional
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(Role.create(role));
            }
        });
    }
}
