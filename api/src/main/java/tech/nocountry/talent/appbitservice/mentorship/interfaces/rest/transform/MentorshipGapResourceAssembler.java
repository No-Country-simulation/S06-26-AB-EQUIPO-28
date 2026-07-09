package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.MentorshipGapResult;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipGapResource;

import java.util.List;

/**
 * Manual assembler that transforms {@link MentorshipGapResult} domain records
 * into {@link MentorshipGapResource} REST DTOs.
 *
 * <p>This assembler needs the repository to resolve {@code matchingProgramIds}
 * into full aggregates, which are then mapped to summary resources via the
 * MapStruct assembler.</p>
 */
@Component
@RequiredArgsConstructor
public class MentorshipGapResourceAssembler {

    private final MentorshipProgramRepository repository;
    private final MentorshipProgramResourceAssembler programAssembler;

    /**
     * Transforms a gap result with program summaries resolved from the DB.
     *
     * @param gapResult the domain gap result
     * @return the REST resource with resolved matching program summaries
     */
    public MentorshipGapResource toResource(MentorshipGapResult gapResult) {
        List<MentorshipProgram> matchingPrograms = gapResult.matchingProgramIds().stream()
                .map(repository::findByProgramId)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList();

        return new MentorshipGapResource(
                gapResult.clusterName(),
                gapResult.vulnerabilityScore(),
                gapResult.vulnerablePopulationCount(),
                gapResult.connectivityLevel(),
                gapResult.hasMentorshipPrograms(),
                matchingPrograms.stream().map(programAssembler::toSummary).toList(),
                gapResult.gapSeverity()
        );
    }
}