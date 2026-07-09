package tech.nocountry.talent.appbitservice.employability.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetMobilityODPairsQuery;
import tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability.MobilityODPairInternalEndpoint;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.MobilityODPairDocs;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairPaginatedResource;

/**
 * REST controller para la matriz OD de movilidad.
 *
 * <p>Delega al {@link MobilityODPairInternalEndpoint} para la orquestación y
 * transformación. Sigue el patrón Gastro Suite: controller = routing only.
 * La validación {@code @Valid} vive en la interfaz docs (no aquí) para evitar
 * el bug HV000151 de Hibernate Validator en interfaces.</p>
 */
@RestController
@RequestMapping("/api/v1/employability")
@RequiredArgsConstructor
public class MobilityODPairController implements MobilityODPairDocs {

    private final MobilityODPairInternalEndpoint internalEndpoint;

    @Override
    @GetMapping("/od-matrix")
    public ResponseEntity<MobilityODPairPaginatedResource> getODPairs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String originCluster,
            @RequestParam(required = false) String destinationCluster,
            @RequestParam(required = false) String predominantPeriod
    ) {
        var query = GetMobilityODPairsQuery.withFilters(
                page, size, sort, originCluster, destinationCluster, predominantPeriod);
        var result = internalEndpoint.getODPairs(query);
        return ResponseEntity.ok(result);
    }
}
