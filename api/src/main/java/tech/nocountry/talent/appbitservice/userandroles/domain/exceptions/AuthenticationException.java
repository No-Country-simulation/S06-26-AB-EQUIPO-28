package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public class AuthenticationException extends UserDomainException {
    public AuthenticationException() {
        super("Usuario o contraseña inválidos");
    }
}