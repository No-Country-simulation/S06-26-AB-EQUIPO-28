package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases.UpdateCitizenProfileCommandUseCase;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.UpdateCitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.CitizenResourceAssembler;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.DemographicsResourceAssembler;

/**
 * Internal endpoint for updating an existing citizen profile.
 *
 * <p>Used by the REST controller to decouple HTTP handling from business logic.</p>
 */
@Component
@RequiredArgsConstructor
public class UpdateCitizenEndpoint {
    private final UpdateCitizenProfileCommandUseCase updateUseCase;
    private final CitizenResourceAssembler commandAssembler;
    private final DemographicsResourceAssembler resourceAssembler;

    /**
     * Handles the update of an existing citizen profile.
     *
     * @param citizenId the citizen hash identifier
     * @param resource  the update resource with fields to update
     * @return the updated citizen resource
     */
    public CitizenResource handle(String citizenId, UpdateCitizenResource resource) {
        var command = commandAssembler.toUpdateCommand(citizenId, resource);
        var citizen = updateUseCase.handle(command);
        return resourceAssembler.toResource(citizen);
    }
}
