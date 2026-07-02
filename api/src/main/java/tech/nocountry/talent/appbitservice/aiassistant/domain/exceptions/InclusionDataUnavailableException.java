package tech.nocountry.talent.appbitservice.aiassistant.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Exception thrown when inclusion core data cannot be retrieved.
 */
public class InclusionDataUnavailableException extends AiAssistantDomainException {
    public InclusionDataUnavailableException(String message) {
        super(ErrorCodes.AIS_DATA_UNAVAILABLE, message);
    }
    public InclusionDataUnavailableException(String message, Throwable cause) {
        super(ErrorCodes.AIS_DATA_UNAVAILABLE, message, cause);
    }
}
