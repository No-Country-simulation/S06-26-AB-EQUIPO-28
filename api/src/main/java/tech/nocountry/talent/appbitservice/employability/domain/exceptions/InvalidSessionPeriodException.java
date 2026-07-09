package tech.nocountry.talent.appbitservice.employability.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando se intenta construir un {@link
 * tech.nocountry.talent.appbitservice.employability.domain.model.valueobjects.SessionPeriod}
 * a partir de un valor presente pero no reconocido.
 */
public class InvalidSessionPeriodException extends EmployabilityDomainException {

    /**
     * Construye la excepción indicando el valor de período de sesión inválido.
     *
     * @param value el valor inválido recibido
     */
    public InvalidSessionPeriodException(String value) {
        super(ErrorCodes.EMP_INVALID_SESSION_PERIOD,
                String.format("Invalid session period: %s. Valid: DAWN, MORNING, AFTERNOON, NIGHT", value));
    }

    /**
     * Construye la excepción con el valor inválido y causa raíz.
     *
     * @param value el valor inválido recibido
     * @param cause causa raíz
     */
    public InvalidSessionPeriodException(String value, Throwable cause) {
        super(ErrorCodes.EMP_INVALID_SESSION_PERIOD,
                String.format("Invalid session period: %s. Valid: DAWN, MORNING, AFTERNOON, NIGHT", value),
                cause);
    }
}