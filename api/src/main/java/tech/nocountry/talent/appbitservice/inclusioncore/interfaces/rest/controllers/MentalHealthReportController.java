package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.internal.mentalhealth.MentalHealthInternalEndpoint;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs.MentalHealthReportDocs;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.MentalHealthReportResource;

/**
 * REST controller for mental health report data.
 *
 * <p>Exposes endpoints to query mental health vulnerability reports.
 * Delegates transformation to the internal endpoint following the Gastro Suite pattern:
 * the Controller only orchestrates, it does not transform.</p>
 */
@RestController
@RequestMapping("/api/v1/inclusion/health-report")
@Validated
@RequiredArgsConstructor
public class MentalHealthReportController implements MentalHealthReportDocs {
    private final MentalHealthInternalEndpoint internalEndpoint;

    @Override
    @GetMapping
    public ResponseEntity<MentalHealthReportResource> getMentalHealthReport(
            @RequestParam(required = false) String reportPeriod,
            @RequestParam(defaultValue = "false") boolean includePriorityOnly
    ) {
        var report = internalEndpoint.getMentalHealthReport(reportPeriod, includePriorityOnly);
        return ResponseEntity.ok(report);
    }
}
