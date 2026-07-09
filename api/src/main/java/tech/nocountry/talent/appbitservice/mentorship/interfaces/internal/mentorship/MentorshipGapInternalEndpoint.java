package tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases.GetMentorshipGapsUseCase;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipGapsQuery;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipGapResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform.MentorshipGapResourceAssembler;

import java.util.List;

/**
 * Internal endpoint for mentorship gap analysis.
 *
 * <p>Orchestrates the gap analysis use case and transforms domain results
 * into REST resources.</p>
 */
@Component
@RequiredArgsConstructor
public class MentorshipGapInternalEndpoint {

    private final GetMentorshipGapsUseCase getGapsUseCase;
    private final MentorshipGapResourceAssembler gapAssembler;

    /**
     * Executes gap analysis: vulnerable clusters vs mentorship program coverage.
     *
     * @param minScore   minimum vulnerability score threshold (0-100)
     * @param maxResults maximum number of results
     * @param focusArea  optional focus area filter (TECH, EMPLOYMENT, etc.)
     * @return list of gap resources sorted by vulnerability desc
     */
    public List<MentorshipGapResource> getGaps(int minScore, int maxResults, String focusArea) {
        var query = new GetMentorshipGapsQuery(minScore, maxResults, focusArea);
        var results = getGapsUseCase.execute(query);
        return results.stream().map(gapAssembler::toResource).toList();
    }
}