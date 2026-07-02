package tech.nocountry.talent.appbitservice.demographics.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción base para errores del dominio de demographics.
 */
public class DemographicsDomainException extends BaseException {
    public DemographicsDomainException(String message) {
        super(ErrorCodes.GEN_INTERNAL_ERROR.getCode(), message);
    }

    public DemographicsDomainException(String message, Throwable cause) {
        super(ErrorCodes.GEN_INTERNAL_ERROR.getCode(), message, cause);
    }
}