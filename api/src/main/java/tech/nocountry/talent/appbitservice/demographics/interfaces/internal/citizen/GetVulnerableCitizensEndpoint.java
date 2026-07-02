package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.application.internal.queryservices.usecases.GetCitizensFilteredQueryUseCase;
import tech.nocountry.talent.appbitservice.demographics.domain.model.queries.GetCitizensFilteredQuery;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.DemographicsResourceAssembler;

import java.util.List;

/**
 * Internal endpoint for retrieving vulnerable citizens (income level D).
 *
 * <p>Used by other Bounded Contexts (e.g., inclusion-core) to access
 * demographics data for social inclusion analysis.</p>
 */
@Component
@RequiredArgsConstructor
public class GetVulnerableCitizensEndpoint {
    private final GetCitizensFilteredQueryUseCase getCitizensFilteredQueryUseCase;
    private final DemographicsResourceAssembler assembler;

    /**
     * Retrieves citizens with income level D (vulnerable population).
     *
     * @return list of vulnerable citizen resources
     */
    public List<CitizenResource> handle() {
        var query = new GetCitizensFilteredQuery("D", null, 0, 200);
        var result = getCitizensFilteredQueryUseCase.handle(query);
        return assembler.toResourceList(result.getContent());
    }
}
