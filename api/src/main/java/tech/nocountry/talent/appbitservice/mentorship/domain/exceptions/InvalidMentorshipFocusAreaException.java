package tech.nocountry.talent.appbitservice.mentorship.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando se intenta construir un
 * {@link tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipFocusArea}
 * con un valor nulo, vacío o fuera del dominio cerrado
 * (TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL).
 */
public class InvalidMentorshipFocusAreaException extends MentorshipDomainException {

    /**
     * Construye la excepción con el mensaje descriptivo del valor inválido.
     *
     * @param message mensaje descriptivo (incluye el valor recibido y los válidos)
     */
    public InvalidMentorshipFocusAreaException(String message) {
        super(ErrorCodes.MEN_INVALID_FOCUS_AREA, message);
    }

    /**
     * Construye la excepción con mensaje y causa raíz.
     *
     * @param message mensaje descriptivo
     * @param cause   causa raíz
     */
    public InvalidMentorshipFocusAreaException(String message, Throwable cause) {
        super(ErrorCodes.MEN_INVALID_FOCUS_AREA, message, cause);
    }
}
