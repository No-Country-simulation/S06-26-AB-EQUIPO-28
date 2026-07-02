package tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetAllUsersQuery;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserSummaryResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllUsersQueryUseCase {
    private final UserRepository userRepository;

    /**
     * Maneja la consulta para obtener usuarios con filtro de busqueda.
     * 
     * @param query la consulta con posible termino de busqueda
     * @return lista de recursos resumidos de usuario
     */
    @Transactional(readOnly = true)
    public List<UserSummaryResource> handle(GetAllUsersQuery query) {
        Objects.requireNonNull(query, "query is required");
        
        String search = query.search();
        List<User> users;
        
        if (search == null || search.isBlank()) {
            users = userRepository.findAllEager();
        } else if (isValidUUID(search)) {
            users = userRepository.findByUserId(UUID.fromString(search));
        } else {
            users = userRepository.findByUsernameContainingOrRole(search);
        }
        
        List<UserSummaryResource> projections = new ArrayList<>();
        
        for (User user : users) {
            projections.add(new UserSummaryResource(
                user.getUserId(), 
                user.getUsername().getValue(), 
                user.getRolesAsStrings(), 
                user.isActive()
            ));
        }
        
        return projections;
    }
    
    private boolean isValidUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}