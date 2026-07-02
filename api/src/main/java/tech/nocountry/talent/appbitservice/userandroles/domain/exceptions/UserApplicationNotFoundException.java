package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

import java.util.UUID;

public class UserApplicationNotFoundException extends UserDomainException {
    public UserApplicationNotFoundException(UUID userId) {
        super(String.format("La aplicación para el usuario con ID %s no fue encontrada", userId));
    }
}