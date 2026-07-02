package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends UserDomainException {
    public UserNotFoundException(UUID userId) {
        super(String.format("El usuario con ID %s no fue encontrado", userId));
    }
}