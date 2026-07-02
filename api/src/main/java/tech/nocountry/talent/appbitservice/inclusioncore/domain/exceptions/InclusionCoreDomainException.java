package tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;

/**
 * Base exception for inclusion-core domain errors.
 */
public class InclusionCoreDomainException extends BaseException {
    public InclusionCoreDomainException(String message) {
        super("INCLUSION_CORE_ERROR", message);
    }

    public InclusionCoreDomainException(String message, Throwable cause) {
        super("INCLUSION_CORE_ERROR", message, cause);
    }
}