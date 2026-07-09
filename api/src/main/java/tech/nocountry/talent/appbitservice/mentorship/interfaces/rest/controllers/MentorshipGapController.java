package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship.MentorshipGapInternalEndpoint;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.MentorshipGapDocs;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipGapResource;

import java.util.List;

/**
 * REST controller for mentorship gap analysis.
 */
@RestController
@RequestMapping("/api/v1/mentorship/gaps")
@RequiredArgsConstructor
public class MentorshipGapController implements MentorshipGapDocs {

    private final MentorshipGapInternalEndpoint internalEndpoint;

    @Override
    @GetMapping
    public ResponseEntity<List<MentorshipGapResource>> getGaps(
            @RequestParam(defaultValue = "15") int minVulnerabilityScore,
            @RequestParam(defaultValue = "30") int maxResults,
            @RequestParam(required = false) String focusArea
    ) {
        var resources = internalEndpoint.getGaps(minVulnerabilityScore, maxResults, focusArea);
        return ResponseEntity.ok(resources);
    }
}