package tech.nocountry.talent.appbitservice.mentorship.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando se intenta construir un
 * {@link tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipModality}
 * con un valor nulo, vacío o fuera del dominio cerrado (REMOTE, IN_PERSON, HYBRID).
 */
public class InvalidMentorshipModalityException extends MentorshipDomainException {

    /**
     * Construye la excepción con el mensaje descriptivo del valor inválido.
     *
     * @param message mensaje descriptivo (incluye el valor recibido y los válidos)
     */
    public InvalidMentorshipModalityException(String message) {
        super(ErrorCodes.MEN_INVALID_MODALITY, message);
    }

    /**
     * Construye la excepción con mensaje y causa raíz.
     *
     * @param message mensaje descriptivo
     * @param cause   causa raíz
     */
    public InvalidMentorshipModalityException(String message, Throwable cause) {
        super(ErrorCodes.MEN_INVALID_MODALITY, message, cause);
    }
}
