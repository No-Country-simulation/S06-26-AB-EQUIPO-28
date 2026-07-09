package tech.nocountry.talent.appbitservice.employability.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetTravelTimesQuery;
import tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability.TravelTimeInternalEndpoint;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.TravelTimeDocs;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimePaginatedResource;

/**
 * REST controller para tiempos de viaje inter-cluster.
 *
 * <p>Delega al {@link TravelTimeInternalEndpoint} para la orquestación y
 * transformación. Sigue el patrón Gastro Suite: controller = routing only.</p>
 */
@RestController
@RequestMapping("/api/v1/employability")
@RequiredArgsConstructor
public class TravelTimeController implements TravelTimeDocs {

    private final TravelTimeInternalEndpoint internalEndpoint;

    @Override
    @GetMapping("/travel-times")
    public ResponseEntity<TravelTimePaginatedResource> getTravelTimes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String originCluster,
            @RequestParam(required = false) String destinationCluster
    ) {
        var query = GetTravelTimesQuery.withFilters(
                page, size, sort, originCluster, destinationCluster);
        var result = internalEndpoint.getTravelTimes(query);
        return ResponseEntity.ok(result);
    }
}
