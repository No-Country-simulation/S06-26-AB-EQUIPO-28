package tech.nocountry.talent.appbitservice.mentorship.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;

/**
 * Atomic command use case to create a new mentorship program.
 *
 * <p>Validates uniqueness of the {@code programId} before calling the aggregate's
 * factory {@link MentorshipProgram#create(CreateMentorshipProgramCommand)}.
 * The transactional boundary ensures that the uniqueness check and the save are
 * atomic — two concurrent creations of the same {@code programId} cannot both succeed.</p>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CreateMentorshipProgramUseCase {

    private final MentorshipProgramRepository repository;

    /**
     * Creates a new mentorship program from a validated command.
     *
     * @param command the creation command with program data
     * @return the persisted {@link MentorshipProgram}
     * @throws IllegalArgumentException if {@code command.programId()} already exists
     */
    public MentorshipProgram execute(CreateMentorshipProgramCommand command) {
        if (repository.existsByProgramId(command.programId())) {
            throw new IllegalArgumentException(
                    "Program ID already exists: " + command.programId());
        }
        MentorshipProgram program = MentorshipProgram.create(command);
        return repository.save(program);
    }
}