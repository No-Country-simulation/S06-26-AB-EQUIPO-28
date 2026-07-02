package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Manejador para excepciones de dominio que extienden BaseException.
 */
@Slf4j
@Component
public class BaseExceptionHandler {

    public ProblemDetail handle(BaseException ex, String path) {
        String errorCode = ex.getCode();

        HttpStatus status = resolveHttpStatus(errorCode);

        if (status.is5xxServerError()) {
            log.error("[{}] {} - ruta: {}", errorCode, ex.getMessage(), path, ex);
        } else {
            log.warn("[{}] {} - ruta: {}", errorCode, ex.getMessage(), path);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                ex.getMessage());
        problemDetail.setProperty("code", errorCode);

        return problemDetail;
    }

    private HttpStatus resolveHttpStatus(String errorCode) {
        ErrorCodes errorCodesEnum = ErrorCodes.fromCode(errorCode);
        if (errorCodesEnum != null) {
            return errorCodesEnum.getStatus();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}