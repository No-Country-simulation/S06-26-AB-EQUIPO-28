package tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions;

/**
 * Exception thrown when an invalid connectivity level is provided.
 */
public class InvalidConnectivityLevelException extends InclusionCoreDomainException {
    public InvalidConnectivityLevelException(String message) {
        super(message);
    }

    public InvalidConnectivityLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}