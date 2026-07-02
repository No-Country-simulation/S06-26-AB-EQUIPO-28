package tech.nocountry.talent.appbitservice.demographics.domain.exceptions;

import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.CitizenId;

/**
 * Excepción lanzada cuando no se encuentra un perfil de ciudadano por su hash.
 */
public class CitizenNotFoundException extends DemographicsDomainException {
    public CitizenNotFoundException(String citizenHash) {
        super(String.format("Citizen with hash %s not found", citizenHash));
    }

    public CitizenNotFoundException(String citizenHash, String message) {
        super(String.format("Citizen with hash %s not found. %s", citizenHash, message));
    }

    public CitizenNotFoundException(String citizenHash, Throwable cause) {
        super(String.format("Citizen with hash %s not found", citizenHash), cause);
    }

    public CitizenNotFoundException(CitizenId citizenId) {
        super(String.format("Citizen with hash %s not found", citizenId.getValue()));
    }
}