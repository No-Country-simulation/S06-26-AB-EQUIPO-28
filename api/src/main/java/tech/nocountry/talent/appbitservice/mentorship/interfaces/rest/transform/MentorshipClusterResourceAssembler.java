package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.MentorshipClusterSummary;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipClusterSummaryResource;

import java.util.List;

/**
 * Manual assembler that transforms {@link MentorshipClusterSummary} domain records
 * into {@link MentorshipClusterSummaryResource} REST DTOs.
 */
@Component
@RequiredArgsConstructor
public class MentorshipClusterResourceAssembler {

    private final MentorshipProgramRepository repository;
    private final MentorshipProgramResourceAssembler programAssembler;

    /**
     * Transforms a cluster summary with program summaries resolved from the DB.
     *
     * @param summary the domain cluster summary
     * @return the REST resource
     */
    public MentorshipClusterSummaryResource toResource(MentorshipClusterSummary summary) {
        List<MentorshipProgram> programs = summary.programIds().stream()
                .map(repository::findByProgramId)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList();

        return new MentorshipClusterSummaryResource(
                summary.clusterName(),
                summary.totalPrograms(),
                summary.activePrograms(),
                summary.focusAreas(),
                summary.modalities(),
                summary.targetAudiences(),
                summary.totalCapacity(),
                summary.totalActiveMentees(),
                programs.stream().map(programAssembler::toSummary).toList()
        );
    }
}