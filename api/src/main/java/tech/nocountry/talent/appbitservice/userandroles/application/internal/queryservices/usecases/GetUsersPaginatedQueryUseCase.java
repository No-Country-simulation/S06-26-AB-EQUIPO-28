package tech.nocountry.talent.appbitservice.userandroles.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates.User;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.queries.GetUsersPaginatedQuery;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserSummaryResource;

@Service
@RequiredArgsConstructor
public class GetUsersPaginatedQueryUseCase {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserSummaryResource> handle(GetUsersPaginatedQuery query) {
        Pageable pageable = Pageable.ofSize(query.size()).withPage(query.page());
        
        Page<User> userPage;
        if (query.search() != null && !query.search().isBlank()) {
            userPage = userRepository.findAllWithSearch(query.search(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.map(user -> new UserSummaryResource(
            user.getUserId(),
            user.getUsername().getValue(),
            user.getRolesAsStrings(),
            user.isActive()
        ));
    }
}