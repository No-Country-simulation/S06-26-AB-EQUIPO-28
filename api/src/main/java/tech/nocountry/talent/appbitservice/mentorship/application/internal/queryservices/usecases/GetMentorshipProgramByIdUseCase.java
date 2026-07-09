package tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.MentorshipProgramNotFoundException;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramByIdQuery;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;

/**
 * Atomic query use case to retrieve a single mentorship program by its business ID.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMentorshipProgramByIdUseCase {

    private final MentorshipProgramRepository repository;

    /**
     * Retrieves a mentorship program by its business identifier.
     *
     * @param query the query containing the {@code programId}
     * @return the matching {@link MentorshipProgram}
     * @throws MentorshipProgramNotFoundException if no program matches the ID
     */
    public MentorshipProgram execute(GetMentorshipProgramByIdQuery query) {
        return repository.findByProgramId(query.programId())
                .orElseThrow(() -> new MentorshipProgramNotFoundException(query.programId()));
    }
}