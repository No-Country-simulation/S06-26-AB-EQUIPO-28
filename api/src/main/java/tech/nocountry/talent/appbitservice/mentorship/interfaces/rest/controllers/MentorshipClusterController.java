package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship.MentorshipClusterInternalEndpoint;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.MentorshipClusterDocs;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipClusterSummaryResource;

import java.util.List;

/**
 * REST controller for mentorship cluster summaries.
 */
@RestController
@RequestMapping("/api/v1/mentorship/clusters")
@RequiredArgsConstructor
public class MentorshipClusterController implements MentorshipClusterDocs {

    private final MentorshipClusterInternalEndpoint internalEndpoint;

    @Override
    @GetMapping
    public ResponseEntity<List<MentorshipClusterSummaryResource>> getClusters(
            @RequestParam(required = false) String focusArea
    ) {
        var resources = internalEndpoint.getClusters(focusArea);
        return ResponseEntity.ok(resources);
    }
}