package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;

/**
 * MapStruct assembler for reverse mapping: Resource → Command.
 *
 * <p>Optional utility for future use — not critical in Phase 3 but required
 * for architectural completeness. The command's {@code focusArea}, {@code modality},
 * and {@code targetAudience} accept raw strings that the aggregate's factory
 * will validate.</p>
 */
@Mapper(componentModel = "spring")
public interface MentorshipProgramCommandAssembler {

    /**
     * Maps a REST resource back to a creation command.
     *
     * @param resource the REST resource
     * @return a create command
     */
    CreateMentorshipProgramCommand fromResource(MentorshipProgramResource resource);
}