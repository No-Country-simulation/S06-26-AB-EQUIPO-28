package tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.demographics.domain.exceptions.CitizenNotFoundException;
import tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates.CitizenProfile;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.UpdateCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories.CitizenProfileRepository;

/**
 * Atomic Use Case: Update an existing citizen profile.
 *
 * <p>Updates an existing citizen profile with new values.</p>
 */
@Service
@RequiredArgsConstructor
public class UpdateCitizenProfileCommandUseCase {
    private final CitizenProfileRepository citizenProfileRepository;

    @Transactional
    public CitizenProfile handle(UpdateCitizenProfileCommand command) {
        var citizen = citizenProfileRepository.findByCitizenIdValue(command.citizenId().getValue())
                .orElseThrow(() -> new CitizenNotFoundException(command.citizenId()));

        citizen.merge(command);

        return citizenProfileRepository.save(citizen);
    }
}