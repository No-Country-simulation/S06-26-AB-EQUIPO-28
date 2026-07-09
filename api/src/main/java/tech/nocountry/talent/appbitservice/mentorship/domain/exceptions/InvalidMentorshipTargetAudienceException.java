package tech.nocountry.talent.appbitservice.mentorship.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando se intenta construir un
 * {@link tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects.MentorshipTargetAudience}
 * con un valor no blank pero fuera del dominio cerrado
 * (YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL).
 *
 * <p><b>Nota</b>: el {@code target_audience} es nullable; un valor {@code null}
 * o blank <b>no</b> lanza esta excepción (devuelve {@code null} en el factory).
 * Esta excepción solo se lanza cuando el valor no es blank pero es inválido.</p>
 */
public class InvalidMentorshipTargetAudienceException extends MentorshipDomainException {

    /**
     * Construye la excepción con el mensaje descriptivo del valor inválido.
     *
     * @param message mensaje descriptivo (incluye el valor recibido y los válidos)
     */
    public InvalidMentorshipTargetAudienceException(String message) {
        super(ErrorCodes.MEN_INVALID_TARGET_AUDIENCE, message);
    }

    /**
     * Construye la excepción con mensaje y causa raíz.
     *
     * @param message mensaje descriptivo
     * @param cause   causa raíz
     */
    public InvalidMentorshipTargetAudienceException(String message, Throwable cause) {
        super(ErrorCodes.MEN_INVALID_TARGET_AUDIENCE, message, cause);
    }
}
