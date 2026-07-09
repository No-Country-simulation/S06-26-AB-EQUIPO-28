package tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases.GetMentorshipClustersUseCase;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipClustersQuery;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipClusterSummaryResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform.MentorshipClusterResourceAssembler;

import java.util.List;

/**
 * Internal endpoint for mentorship cluster summaries.
 */
@Component
@RequiredArgsConstructor
public class MentorshipClusterInternalEndpoint {

    private final GetMentorshipClustersUseCase getClustersUseCase;
    private final MentorshipClusterResourceAssembler clusterAssembler;

    /**
     * Retrieves cluster summaries, optionally filtered by focus area.
     *
     * @param focusArea optional focus area filter (null = all)
     * @return list of cluster summary resources sorted alphabetically by cluster name
     */
    public List<MentorshipClusterSummaryResource> getClusters(String focusArea) {
        var query = GetMentorshipClustersQuery.withFocusArea(focusArea);
        var results = getClustersUseCase.execute(query);
        return results.stream().map(clusterAssembler::toResource).toList();
    }
}