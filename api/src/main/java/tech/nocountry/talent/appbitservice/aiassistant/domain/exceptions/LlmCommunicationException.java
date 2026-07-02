package tech.nocountry.talent.appbitservice.aiassistant.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Exception thrown when communication with the LLM fails.
 */
public class LlmCommunicationException extends AiAssistantDomainException {
    public LlmCommunicationException(String message) {
        super(ErrorCodes.AIS_LLM_UNAVAILABLE, message);
    }
    public LlmCommunicationException(String message, Throwable cause) {
        super(ErrorCodes.AIS_LLM_UNAVAILABLE, message, cause);
    }
}
