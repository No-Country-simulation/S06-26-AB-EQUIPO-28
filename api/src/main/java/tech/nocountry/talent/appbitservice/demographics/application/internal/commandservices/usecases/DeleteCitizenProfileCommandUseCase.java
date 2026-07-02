package tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.demographics.domain.exceptions.CitizenNotFoundException;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.DeleteCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories.CitizenProfileRepository;

/**
 * Atomic Use Case: Delete a citizen profile.
 *
 * <p>Removes a citizen profile from the database.</p>
 */
@Service
@RequiredArgsConstructor
public class DeleteCitizenProfileCommandUseCase {
    private final CitizenProfileRepository citizenProfileRepository;

    @Transactional
    public void handle(DeleteCitizenProfileCommand command) {
        var citizen = citizenProfileRepository.findByCitizenIdValue(command.citizenId().getValue())
                .orElseThrow(() -> new CitizenNotFoundException(command.citizenId()));
        citizenProfileRepository.delete(citizen);
    }
}