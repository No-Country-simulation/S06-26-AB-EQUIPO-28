package tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.FailedToDeleteUserException;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserApplicationNotFoundException;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.DeleteUserCommand;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteUserCommandUseCase {
    private final UserRepository userRepository;

    @Transactional
    public void handle(DeleteUserCommand command) {
        Objects.requireNonNull(command, "command is required");
        Objects.requireNonNull(command.userId(), "userId is required");

        var user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserApplicationNotFoundException(command.userId()));

        try {
            userRepository.delete(user);
        } catch (DataAccessException ex) {
            throw new FailedToDeleteUserException("Failed to delete user", ex);
        }
    }
}
