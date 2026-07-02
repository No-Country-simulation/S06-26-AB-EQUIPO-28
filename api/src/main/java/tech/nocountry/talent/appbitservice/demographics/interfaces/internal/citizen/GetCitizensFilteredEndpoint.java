package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.application.internal.queryservices.usecases.GetCitizensFilteredQueryUseCase;
import tech.nocountry.talent.appbitservice.demographics.domain.model.queries.GetCitizensFilteredQuery;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenPaginatedResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.DemographicsResourceAssembler;

/**
 * Internal endpoint for retrieving filtered and paginated citizens.
 *
 * <p>Unifies the previous individual endpoints into a single endpoint that accepts
 * optional {@code incomeLevel} and {@code ageGroup} filters with mandatory pagination.</p>
 */
@Component
@RequiredArgsConstructor
public class GetCitizensFilteredEndpoint {

    private final GetCitizensFilteredQueryUseCase useCase;
    private final DemographicsResourceAssembler assembler;

    /**
     * Retrieves a paginated page of citizens matching the given filters.
     *
     * @param incomeLevel optional income level filter (A, B, C, D)
     * @param ageGroup    optional age group filter (18-24, 25-34, 35-44, 45-54, 55+)
     * @param page        page number (0-based)
     * @param size        page size (1-100)
     * @return paginated citizen resource with metadata
     */
    public CitizenPaginatedResource handle(String incomeLevel, String ageGroup, int page, int size) {
        var query = new GetCitizensFilteredQuery(incomeLevel, ageGroup, page, size);
        var result = useCase.handle(query);
        return assembler.toPaginatedResource(result);
    }
}
