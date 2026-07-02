package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna.GetAllAntennasEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna.GetAntennaByEcgiEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.AntennaDocs;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;

/**
 * REST controller para el recurso de antenas de telecomunicación.
 *
 * <p>Expone endpoints para consultar datos de antenas.
 * Delega la transformación a los internal endpoints siguiendo el patrón de Gastro Suite:
 * el Controller solo orquesta, no transforma.</p>
 */
@RestController
@RequestMapping("/api/v1/telemetry/antennas")
@RequiredArgsConstructor
public class AntennaController implements AntennaDocs {
    private final GetAllAntennasEndpoint getAllAntennasEndpoint;
    private final GetAntennaByEcgiEndpoint getAntennaByEcgiEndpoint;

    @Override
    @GetMapping
    public ResponseEntity<AntennaPaginatedResource> getAllAntennas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(getAllAntennasEndpoint.handle(page, size));
    }

    @Override
    @GetMapping("/{ecgi}")
    public ResponseEntity<AntennaResource> getAntennaByEcgi(@PathVariable String ecgi) {
        var resource = getAntennaByEcgiEndpoint.handle(ecgi);
        if (resource == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(resource);
    }
}
