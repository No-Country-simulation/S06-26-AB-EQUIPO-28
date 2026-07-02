package tech.nocountry.talent.appbitservice.demographics.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates.CitizenProfile;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.CreateCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories.CitizenProfileRepository;

/**
 * Atomic Use Case: Create a new citizen profile.
 *
 * <p>Persists a new citizen profile to the database.</p>
 */
@Service
@RequiredArgsConstructor
public class CreateCitizenProfileCommandUseCase {
    private final CitizenProfileRepository citizenProfileRepository;

    @Transactional
    public CitizenProfile handle(CreateCitizenProfileCommand command) {
        var citizen = new CitizenProfile(
                command.citizenId(),
                command.incomeLevel(),
                command.ageGroup(),
                command.mobilityPattern(),
                command.homeCluster()
        );
        return citizenProfileRepository.save(citizen);
    }
}