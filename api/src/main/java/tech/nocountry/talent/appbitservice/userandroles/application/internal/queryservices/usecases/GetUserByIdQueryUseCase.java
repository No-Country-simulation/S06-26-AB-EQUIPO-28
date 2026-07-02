package tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetUserByIdQuery;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserSummaryResource;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserByIdQueryUseCase {
    private final UserRepository userRepository;

    /**
     * Maneja la consulta para obtener un usuario por ID.
     * 
     * @param query la consulta con el ID del usuario
     * @return un Optional con el recurso resumido, o vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<UserSummaryResource> handle(GetUserByIdQuery query) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(query.userId(), "userId is required");

        Optional<User> userOpt = userRepository.findById(query.userId());

        if (userOpt.isEmpty()) { 
            return Optional.empty(); 
        }

        User user = userOpt.get();
        return Optional.of(new UserSummaryResource(
            user.getUserId(), 
            user.getUsername().getValue(), 
            user.getRolesAsStrings(), 
            user.isActive()
        ));
    }
}