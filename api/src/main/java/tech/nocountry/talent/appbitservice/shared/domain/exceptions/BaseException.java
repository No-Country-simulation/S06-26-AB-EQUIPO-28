package tech.nocountry.talent.appbitservice.shared.domain.exceptions;

import lombok.Getter;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepcion base simplificada para errores globales.
 * Solo tiene el codigo de error y el mensaje.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final String code;

    protected BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /** Constructor compatibilidad con ErrorCodes enum */
    protected BaseException(ErrorCodes errorCode, String userMessage) {
        super(userMessage);
        this.code = errorCode.getCode();
    }

    /** Constructor compatibilidad con ErrorCodes enum y causa */
    protected BaseException(ErrorCodes errorCode, String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.code = errorCode.getCode();
    }
}