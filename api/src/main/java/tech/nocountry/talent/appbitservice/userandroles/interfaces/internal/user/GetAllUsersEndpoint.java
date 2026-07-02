package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases.GetAllUsersQueryUseCase;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases.GetUsersPaginatedQueryUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetAllUsersQuery;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetUsersPaginatedQuery;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.UserResourceFromProjectionAssembler;

import java.util.List;

/**
 * Endpoint interno para obtener usuarios.
 * Transforma la consulta, ejecuta el caso de uso de consulta y transforma la respuesta.
 */
@Component
@RequiredArgsConstructor
public class GetAllUsersEndpoint {
    private final GetAllUsersQueryUseCase getAllUsersUseCase;
    private final GetUsersPaginatedQueryUseCase getUsersPaginatedUseCase;
    private final UserResourceFromProjectionAssembler userResourceAssembler;

    /**
     * Obtiene todos los usuarios sin paginación.
     *
     * @return lista de recursos de usuario
     */
    public List<UserResource> handle() {
        var query = new GetAllUsersQuery();
        var users = getAllUsersUseCase.handle(query);
        return userResourceAssembler.toResourceList(users);
    }

    /**
     * Obtiene usuarios con paginación.
     *
     * @param page número de página (comienza en 0)
     * @param size tamaño de página
     * @param search término de búsqueda (opcional)
     * @return página de recursos de usuario
     */
    public Page<UserResource> handlePaginated(Integer page, Integer size, String search) {
        var query = new GetUsersPaginatedQuery(page, size, search);
        var usersPage = getUsersPaginatedUseCase.handle(query);
        return usersPage.map(userResourceAssembler::toResource);
    }
}