package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramPaginatedResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramSummaryResource;

/**
 * MapStruct assembler that transforms {@link MentorshipProgram} aggregate roots
 * into REST resources.
 *
 * <p>Value Objects ({@code focusArea}, {@code modality}, {@code targetAudience})
 * are mapped via {@code getValue()} — the assembler navigates the embedded record
 * fields automatically because MapStruct reads the getter chain:
 * {@code program.getFocusArea().getValue() → focusArea}.</p>
 *
 * <p>The {@code createdAt} and {@code updatedAt} fields come from the
 * {@code AuditableAbstractAggregateRoot} superclass and are mapped directly.</p>
 */
@Mapper(componentModel = "spring")
public interface MentorshipProgramResourceAssembler {

    /**
     * Maps a full aggregate to a complete REST resource.
     *
     * @param program the aggregate root
     * @return the full resource DTO
     */
    @Mapping(source = "focusArea.value", target = "focusArea")
    @Mapping(source = "modality.value", target = "modality")
    @Mapping(source = "targetAudience.value", target = "targetAudience")
    MentorshipProgramResource toResource(MentorshipProgram program);

    /**
     * Maps an aggregate to a lightweight summary resource.
     *
     * @param program the aggregate root
     * @return the summary DTO (programId, name, focusArea, modality, isActive)
     */
    @Mapping(source = "focusArea.value", target = "focusArea")
    @Mapping(source = "modality.value", target = "modality")
    MentorshipProgramSummaryResource toSummary(MentorshipProgram program);

    /**
     * Transforms a {@link Page} of {@link MentorshipProgram} aggregates into a
     * paginated resource.
     *
     * <p>Default MapStruct method: maps each aggregate to a {@link MentorshipProgramResource}
     * and wraps the result list with pagination metadata, following the same
     * contract as {@code AntennaResourceAssembler#toPaginatedResource}.</p>
     *
     * @param page the page of domain aggregates
     * @return the paginated REST resource
     */
    default MentorshipProgramPaginatedResource toPaginatedResource(Page<MentorshipProgram> page) {
        return new MentorshipProgramPaginatedResource(
                page.getContent().stream().map(this::toResource).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}