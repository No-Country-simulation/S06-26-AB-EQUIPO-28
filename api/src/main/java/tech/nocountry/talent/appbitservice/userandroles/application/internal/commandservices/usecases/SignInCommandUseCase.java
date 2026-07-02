package tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.hashing.HashingService;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.tokens.TokenService;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.AuthenticationException;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SignInCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserName;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SignInCommandUseCase {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    @Transactional(readOnly = true)
    public ImmutablePair<User, String> handle(SignInCommand command) {
        Objects.requireNonNull(command, "command is required");
        Objects.requireNonNull(command.username(), "username is required");
        Objects.requireNonNull(command.password(), "password is required");

        var user = userRepository.findByUsername(new UserName(command.username()))
            .orElseThrow(AuthenticationException::new);

        if (!hashingService.matches(command.password(), user.getPassword().getValue())) {
            throw new AuthenticationException();
        }

        if (!user.isActive()) {
            throw new AuthenticationException();
        }

        var roles = user.getRolesAsStrings();
        var token = tokenService.generateToken(user.getUsername().getValue(), roles, user.getUserId());
        return ImmutablePair.of(user, token);
    }
}
