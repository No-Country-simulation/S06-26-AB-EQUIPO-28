package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public class FailedToUpdateUserException extends UserDomainException {
    public FailedToUpdateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}