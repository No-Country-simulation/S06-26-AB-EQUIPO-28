package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases.DeleteCitizenProfileCommandUseCase;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform.CitizenResourceAssembler;

/**
 * Internal endpoint for deleting a citizen profile.
 *
 * <p>Used by the REST controller to decouple HTTP handling from business logic.</p>
 */
@Component
@RequiredArgsConstructor
public class DeleteCitizenEndpoint {
    private final DeleteCitizenProfileCommandUseCase deleteUseCase;
    private final CitizenResourceAssembler commandAssembler;

    /**
     * Handles the deletion of a citizen profile.
     *
     * @param citizenId the citizen hash identifier to delete
     */
    public void handle(String citizenId) {
        var command = commandAssembler.toDeleteCommand(citizenId);
        deleteUseCase.handle(command);
    }
}
