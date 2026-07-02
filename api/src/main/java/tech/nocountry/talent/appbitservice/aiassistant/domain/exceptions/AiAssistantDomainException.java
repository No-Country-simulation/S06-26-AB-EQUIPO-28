package tech.nocountry.talent.appbitservice.aiassistant.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Base exception for errors in the AI Assistant domain.
 * Extends {@link BaseException} so that the {@code GlobalExceptionHandler}
 * maps it to the proper HTTP status code via {@link ErrorCodes}.
 */
public class AiAssistantDomainException extends BaseException {
    public AiAssistantDomainException(ErrorCodes errorCode, String message) {
        super(errorCode, message);
    }

    public AiAssistantDomainException(ErrorCodes errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
