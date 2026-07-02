package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.CreateCitizenEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.DeleteCitizenEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.GetCitizensFilteredEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.UpdateCitizenEndpoint;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.DemographicsDocs;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenPaginatedResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CreateCitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.UpdateCitizenResource;

/**
 * REST controller for demographics data exposure.
 *
 * <p>Exposes endpoints for querying and managing citizen demographic profiles.
 * Delegates all business logic to Internal Endpoints.</p>
 */
@RestController
@RequestMapping("/api/v1/demographics")
@RequiredArgsConstructor
@Validated
@Profile("dev")
public class DemographicsController implements DemographicsDocs {

    private final GetCitizensFilteredEndpoint getCitizensFilteredEndpoint;
    private final CreateCitizenEndpoint createCitizenEndpoint;
    private final UpdateCitizenEndpoint updateCitizenEndpoint;
    private final DeleteCitizenEndpoint deleteCitizenEndpoint;

    // ------------------------------------
    // Query Endpoints — unified
    // ------------------------------------

    @Override
    @GetMapping("/citizens")
    public ResponseEntity<CitizenPaginatedResource> getAllCitizens(
            @RequestParam(required = false) String incomeLevel,
            @RequestParam(required = false) String ageGroup,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(getCitizensFilteredEndpoint.handle(incomeLevel, ageGroup, page, size));
    }

    // -----------------------------------
    // Command Endpoints
    // -----------------------------------

    @Override
    @PostMapping("/citizens")
    public ResponseEntity<CitizenResource> createCitizen(@RequestBody CreateCitizenResource resource) {
        var citizenResource = createCitizenEndpoint.handle(resource);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/api/v1/demographics/citizens/" + citizenResource.citizenHash())
                .body(citizenResource);
    }

    @Override
    @PutMapping("/citizens/{citizenId}")
    public ResponseEntity<CitizenResource> updateCitizen(
            @PathVariable String citizenId,
            @RequestBody UpdateCitizenResource resource
    ) {
        return ResponseEntity.ok(updateCitizenEndpoint.handle(citizenId, resource));
    }

    @Override
    @DeleteMapping("/citizens/{citizenId}")
    public ResponseEntity<Void> deleteCitizen(@PathVariable String citizenId) {
        deleteCitizenEndpoint.handle(citizenId);
        return ResponseEntity.noContent().build();
    }
}