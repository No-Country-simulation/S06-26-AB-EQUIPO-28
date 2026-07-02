package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

import java.net.URI;

/**
 * Manejador para excepciones de autenticacion.
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler {
    public ProblemDetail handle(BadCredentialsException ex, String path) {
        log.warn("Error de autenticación en la ruta {}: {}", path, ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Credenciales inválidas. Verifica tu usuario y contraseña.");
        problemDetail.setType(URI.create("https://gastro-suite.com/errors/auth-invalid-credentials"));
        problemDetail.setProperty("code", ErrorCodes.AUTH_UNAUTHORIZED.getCode());

        return problemDetail;
    }
}