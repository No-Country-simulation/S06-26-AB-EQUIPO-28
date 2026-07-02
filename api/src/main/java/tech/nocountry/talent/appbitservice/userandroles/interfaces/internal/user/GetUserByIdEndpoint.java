package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases.GetUserByIdQueryUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserNotFoundException;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetUserByIdQuery;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.UserResourceFromProjectionAssembler;

import java.util.UUID;

/**
 * Endpoint interno para obtener un usuario por su ID.
 * Transforma la consulta, ejecuta el caso de uso de consulta y transforma la respuesta.
 */
@Component
@RequiredArgsConstructor
public class GetUserByIdEndpoint {
    private final GetUserByIdQueryUseCase getUserByIdUseCase;
    private final UserResourceFromProjectionAssembler userResourceAssembler;

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId ID del usuario
     * @return recurso del usuario
     * @throws UserNotFoundException si no se encuentra el usuario
     */
    public UserResource handle(UUID userId) {
        var query = new GetUserByIdQuery(userId);
        var userSummary = getUserByIdUseCase.handle(query);
        return userSummary
                .map(userResourceAssembler::toResource)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}