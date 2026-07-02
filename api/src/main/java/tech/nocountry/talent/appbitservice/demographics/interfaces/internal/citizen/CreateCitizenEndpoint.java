package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases.CreateCitizenProfileCommandUseCase;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CreateCitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.CitizenResourceAssembler;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.DemographicsResourceAssembler;

/**
 * Internal endpoint for creating a new citizen profile.
 *
 * <p>Used by the REST controller to decouple HTTP handling from business logic.</p>
 */
@Component
@RequiredArgsConstructor
public class CreateCitizenEndpoint {
    private final CreateCitizenProfileCommandUseCase createUseCase;
    private final CitizenResourceAssembler commandAssembler;
    private final DemographicsResourceAssembler resourceAssembler;

    /**
     * Handles the creation of a new citizen profile.
     *
     * @param resource the create resource with citizen data
     * @return the created citizen resource
     */
    public CitizenResource handle(CreateCitizenResource resource) {
        var command = commandAssembler.toCreateCommand(resource);
        var citizen = createUseCase.handle(command);
        return resourceAssembler.toResource(citizen);
    }
}
