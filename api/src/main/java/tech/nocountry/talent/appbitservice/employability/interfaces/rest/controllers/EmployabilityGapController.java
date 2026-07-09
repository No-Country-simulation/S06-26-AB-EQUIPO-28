package tech.nocountry.talent.appbitservice.employability.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability.EmployabilityGapInternalEndpoint;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.EmployabilityGapDocs;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.EmployabilityGapResource;

import java.util.List;

/**
 * REST controller para el análisis de brechas de empleabilidad.
 *
 * <p>Delega al {@link EmployabilityGapInternalEndpoint} para la orquestación y
 * transformación. Sigue el patrón Gastro Suite: controller = routing only.</p>
 */
@RestController
@RequestMapping("/api/v1/employability")
@RequiredArgsConstructor
public class EmployabilityGapController implements EmployabilityGapDocs {

    private final EmployabilityGapInternalEndpoint internalEndpoint;

    @Override
    @GetMapping("/gaps")
    public ResponseEntity<List<EmployabilityGapResource>> getGaps(
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(required = false) String minSeverity,
            @RequestParam(required = false) String cluster,
            @RequestParam(defaultValue = "false") boolean onlyBlindZones
    ) {
        var resources = internalEndpoint.getGaps(maxResults, minSeverity, cluster, onlyBlindZones);
        return ResponseEntity.ok(resources);
    }
}
