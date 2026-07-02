package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public abstract class UserDomainException extends RuntimeException {
    protected UserDomainException(String message) {
        super(message);
    }

    protected UserDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}