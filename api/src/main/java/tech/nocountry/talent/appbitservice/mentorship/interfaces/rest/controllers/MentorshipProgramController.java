package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramsQuery;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship.MentorshipProgramInternalEndpoint;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.MentorshipProgramDocs;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramPaginatedResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;

import java.net.URI;

/**
 * REST controller for mentorship program CRUD operations.
 *
 * <p>Delegates to {@link MentorshipProgramInternalEndpoint} for orchestration
 * and transformation. Follows the Gastro Suite pattern: controller = routing only.</p>
 */
@RestController
@RequestMapping("/api/v1/mentorship")
@Validated
@RequiredArgsConstructor
public class MentorshipProgramController implements MentorshipProgramDocs {

    private final MentorshipProgramInternalEndpoint internalEndpoint;

    @Override
    @GetMapping("/programs")
    public ResponseEntity<MentorshipProgramPaginatedResource> getPrograms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String focusArea,
            @RequestParam(required = false) String modality,
            @RequestParam(required = false) String clusterName,
            @RequestParam(required = false) String targetIncomeLevel,
            @RequestParam(required = false) Boolean isActive
    ) {
        var query = GetMentorshipProgramsQuery.withFilters(
                page, size, sort, focusArea, modality, clusterName, targetIncomeLevel, isActive);
        var result = internalEndpoint.getPrograms(query);
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/programs/{programId}")
    public ResponseEntity<MentorshipProgramResource> getProgramByProgramId(
            @PathVariable String programId
    ) {
        var resource = internalEndpoint.getProgramByProgramId(programId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @PostMapping("/programs")
    public ResponseEntity<MentorshipProgramResource> createProgram(
            @RequestBody CreateMentorshipProgramCommand command
    ) {
        var saved = internalEndpoint.createProgram(command);
        var location = URI.create("/api/v1/mentorship/programs/" + saved.programId());
        return ResponseEntity.created(location).body(saved);
    }
}