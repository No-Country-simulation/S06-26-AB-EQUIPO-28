package tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases.GetAllRolesQueryUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetAllRolesQuery;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RoleResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform.RoleResourceFromEntityAssembler;

import java.util.List;

/**
 * Internal endpoint for retrieving all roles.
 *
 * <p>Used by the REST controller to decouple HTTP handling from business logic.</p>
 */
@Component
@RequiredArgsConstructor
public class GetAllRolesEndpoint {
    private final GetAllRolesQueryUseCase getAllRolesQueryUseCase;
    private final RoleResourceFromEntityAssembler assembler;

    /**
     * Retrieves all roles.
     *
     * @return list of role resources
     */
    public List<RoleResource> handle() {
        var roles = getAllRolesQueryUseCase.handle(new GetAllRolesQuery());
        return assembler.toResourceList(roles);
    }
}
