package tech.nocountry.talent.appbitservice.mentorship.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción base del bounded context de mentorías (MEN).
 *
 * <p>A diferencia del patrón del BC inclusion-core (que usa un string literal
 * como código), esta base ancla el código al enum {@link ErrorCodes} estándar
 * del proyecto, garantizando trazabilidad uniforme de errores entre BCs y
 * mapeo consistente a {@code ProblemDetail} (RFC 7807) en la capa de
 * interfaces REST.</p>
 *
 * <p>Usa el constructor {@link BaseException#BaseException(ErrorCodes, String)}
 * que recibe el {@link ErrorCodes} y un mensaje específico del error concreto.
 * El código HTTP asociado se resuelve desde el propio {@link ErrorCodes}.</p>
 */
public class MentorshipDomainException extends BaseException {

    /**
     * Construye una excepción de dominio de mentorías con código por defecto
     * {@link ErrorCodes#MEN_GENERAL_ERROR}.
     *
     * @param message mensaje específico del error
     */
    public MentorshipDomainException(String message) {
        super(ErrorCodes.MEN_GENERAL_ERROR, message);
    }

    /**
     * Construye una excepción de dominio de mentorías con código por defecto
     * {@link ErrorCodes#MEN_GENERAL_ERROR} y causa raíz.
     *
     * @param message mensaje específico del error
     * @param cause   causa raíz
     */
    public MentorshipDomainException(String message, Throwable cause) {
        super(ErrorCodes.MEN_GENERAL_ERROR, message, cause);
    }

    /**
     * Construye una excepción de dominio de mentorías con un {@link ErrorCodes}
     * específico (para que las subclases indiquen su código concreto).
     *
     * @param errorCode código de error del enum
     * @param message   mensaje específico del error
     */
    protected MentorshipDomainException(ErrorCodes errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Construye una excepción de dominio de mentorías con un {@link ErrorCodes}
     * específico y causa raíz.
     *
     * @param errorCode código de error del enum
     * @param message   mensaje específico del error
     * @param cause     causa raíz
     */
    protected MentorshipDomainException(ErrorCodes errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
