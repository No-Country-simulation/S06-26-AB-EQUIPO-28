package tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.hashing.HashingService;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.RoleNotFoundException;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserAlreadyExistsException;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserCreationException;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.CreateUserCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SignUpCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Password;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserName;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.RoleRepository;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignUpCommandUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;

    @Transactional
    public UUID handle(SignUpCommand command) {
        Objects.requireNonNull(command, "command is required");
        
        if (command.roles() == null || command.roles().isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un rol");
        }

        var tempUsername = new UserName(command.username());
        if (userRepository.existsByUsername(tempUsername)) {
            throw new UserAlreadyExistsException();
        }

        List<Role> validRoles = command.roles().stream()
            .map(role -> {
                var roleName = Roles.valueOf(role.getStringName());
                return roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(role.getStringName()));
            })
            .toList();

        if (!Password.isValid(command.password())) {
            throw new IllegalArgumentException("Formato de contraseña inválido");
        }

        var hashedPassword = hashingService.encode(command.password());
        
        var user = new User(new CreateUserCommand(
            command.username(),
            hashedPassword,
            command.isActive(),
            validRoles.getFirst()
        ));

        for (int i = 1; i < validRoles.size(); i++) {
            user.addRole(validRoles.get(i));
        }

        try {
            var savedUser = userRepository.save(user);
            return savedUser.getUserId();
        } catch (DataAccessException ex) {
            throw new UserCreationException("Failed to create user", ex);
        }
    }
}
