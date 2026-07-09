package tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.mentorship.application.internal.commandservices.usecases.CreateMentorshipProgramUseCase;
import tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases.GetMentorshipProgramByIdUseCase;
import tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases.GetMentorshipProgramsUseCase;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramByIdQuery;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramsQuery;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramPaginatedResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform.MentorshipProgramResourceAssembler;

/**
 * Internal endpoint for mentorship program operations.
 *
 * <p>Orchestrates use cases and transforms domain results into REST resources
 * using assemblers. Follows the Gastro Suite pattern: the REST controller only
 * delegates to this internal endpoint; actual transformation lives here.</p>
 */
@Component
@RequiredArgsConstructor
public class MentorshipProgramInternalEndpoint {

    private final CreateMentorshipProgramUseCase createProgramUseCase;
    private final GetMentorshipProgramsUseCase getProgramsUseCase;
    private final GetMentorshipProgramByIdUseCase getProgramByIdUseCase;
    private final MentorshipProgramResourceAssembler assembler;

    /**
     * Creates a new mentorship program.
     *
     * @param command the creation command
     * @return the created program as a REST resource
     */
    public MentorshipProgramResource createProgram(CreateMentorshipProgramCommand command) {
        var program = createProgramUseCase.execute(command);
        return assembler.toResource(program);
    }

    /**
     * Retrieves a paginated list of programs with optional filters.
     *
     * <p>Builds the custom {@link MentorshipProgramPaginatedResource} wrapper from
     * the Spring Data {@link org.springframework.data.domain.Page} returned by the
     * use case, following the repository-wide pagination pattern.</p>
     *
     * @param query the paginated query
     * @return the paginated program resources
     */
    public MentorshipProgramPaginatedResource getPrograms(GetMentorshipProgramsQuery query) {
        var result = getProgramsUseCase.execute(query);
        return assembler.toPaginatedResource(result);
    }

    /**
     * Retrieves a single program by its business identifier.
     *
     * @param programId the business program ID
     * @return the program resource
     * @throws tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.MentorshipProgramNotFoundException if not found
     */
    public MentorshipProgramResource getProgramByProgramId(String programId) {
        var query = GetMentorshipProgramByIdQuery.of(programId);
        var program = getProgramByIdUseCase.execute(query);
        return assembler.toResource(program);
    }
}