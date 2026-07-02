package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Manejador para excepciones de base de datos.
 */
@Slf4j
@Component
public class DatabaseExceptionHandler {
    public ProblemDetail handle(DataAccessException ex, String path) {
        log.error("Error de base de datos en la ruta {}: {}", path, ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno al procesar la solicitud. El equipo tecnico ha sido notificado.");
        problemDetail.setProperty("code", ErrorCodes.GEN_INTERNAL_ERROR.getCode());

        return problemDetail;
    }
}