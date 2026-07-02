package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public class UserAlreadyExistsException extends UserDomainException {
    public UserAlreadyExistsException() {
        super("Ya existe una cuenta con los datos proporcionados");
    }
}