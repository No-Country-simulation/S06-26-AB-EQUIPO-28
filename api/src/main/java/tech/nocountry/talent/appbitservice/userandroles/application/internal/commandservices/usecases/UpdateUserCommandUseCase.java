package tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.hashing.HashingService;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.FailedToUpdateUserException;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.RoleNotFoundException;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserApplicationNotFoundException;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.UpdateUserCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Password;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserSummary;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.RoleRepository;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateUserCommandUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;

    @Transactional
    public UserSummary handle(UpdateUserCommand command) {
        Objects.requireNonNull(command, "command is required");
        Objects.requireNonNull(command.userId(), "userId is required");

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserApplicationNotFoundException(command.userId()));

        if (command.roles() != null) {
            List<Role> newRoles = command.roles().stream()
                .map(roleName -> roleRepository.findByName(Roles.valueOf(roleName))
                    .orElseThrow(() -> new RoleNotFoundException(roleName)))
                .toList();
            user.replaceRoles(newRoles);
        }

        if (command.password() != null && !command.password().equals("KEEP_EXISTING_PASSWORD")) {
            if (!Password.isValid(command.password())) {
                throw new IllegalArgumentException("Formato de contrasena invalido");
            }
            user.updateUser(command.username(), hashingService.encode(command.password()), command.isActive());
        } else {
            String currentUsername = user.getUsername().getValue();
            String newUsername = (command.username() != null && !command.username().equals(currentUsername)) 
                ? command.username() : null;
            Boolean newIsActive = (command.isActive() != null && !command.isActive().equals(user.isActive())) 
                ? command.isActive() : null;

            if (newUsername != null || newIsActive != null) {
                user.updateIdentifierAndStatus(
                    newUsername != null ? newUsername : currentUsername,
                    newIsActive != null ? newIsActive : user.isActive()
                );
            }
        }

        try {
            User savedUser = userRepository.save(user);
            return new UserSummary(
                savedUser.getUserId(),
                savedUser.getUsername().getValue(),
                savedUser.getRolesAsStrings(),
                savedUser.isActive()
            );
        } catch (DataAccessException ex) {
            throw new FailedToUpdateUserException("Failed to update user", ex);
        }
    }
}