package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration.GetConcentrationFilteredEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.ConcentrationDocs;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationPaginatedResource;

import java.time.LocalDate;

/**
 * REST controller para el recurso de métricas de concentración de red.
 *
 * <p>Expone endpoints para consultar métricas de concentración de red.
 * Delega la transformación a los internal endpoints siguiendo el patrón de Gastro Suite:
 * el Controller solo orquesta, no transforma.</p>
 */
@RestController
@RequestMapping("/api/v1/telemetry/concentration")
@RequiredArgsConstructor
public class ConcentrationController implements ConcentrationDocs {
    private final GetConcentrationFilteredEndpoint getConcentrationFilteredEndpoint;

    @Override
    @GetMapping
    public ResponseEntity<ConcentrationPaginatedResource> getConcentration(
            @RequestParam(required = false) String cluster,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                getConcentrationFilteredEndpoint.handle(cluster, startDate, endDate, period, page, size)
        );
    }
}
