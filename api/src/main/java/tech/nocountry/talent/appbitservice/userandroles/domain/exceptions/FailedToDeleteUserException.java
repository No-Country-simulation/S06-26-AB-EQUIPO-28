package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public class FailedToDeleteUserException extends UserDomainException {
    public FailedToDeleteUserException(String message, Throwable cause) {
        super(message, cause);
    }
}