package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.transform;

import lombok.extern.slf4j.Slf4j;

/**
 * Error assembler for the Demographics ACL — employability side.
 *
 * <p>Intercepts exceptions raised by the in-process demographics integration
 * and translates them into domain-appropriate exceptions for the employability
 * BC, preserving the root cause for debugging.</p>
 *
 * <p>Follows the same structure as
 * {@code mentorship.interfaces.acl.inclusioncore.transform.InclusionCoreErrorAssembler}:
 * since this is an in-process (non-HTTP) integration, the discriminator is the
 * exception type rather than HTTP status codes.</p>
 */
@Slf4j
public final class EmployabilityDemographicsErrorAssembler {

    private EmployabilityDemographicsErrorAssembler() {
    }

    /**
     * Translates a technical exception into a domain-appropriate exception for
     * the employability BC.
     *
     * @param cause   the original exception from the demographics integration
     * @param context human-readable context for the error message
     * @return a RuntimeException subclass appropriate to the error type
     */
    public static RuntimeException assemble(Exception cause, String context) {
        log.error("Error en comunicación con Demographics BC: {}", context, cause);

        String message = String.format("Error al obtener datos demográficos para employability: %s", context);

        if (cause instanceof IllegalArgumentException) {
            return new DemographicsResourceNotFoundException(
                    String.format("Parámetros inválidos para demographics: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        if (cause instanceof IllegalStateException) {
            return new DemographicsServiceUnavailableException(
                    String.format("Demographics BC no disponible: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        return new DemographicsAccessException(message, cause);
    }

    /**
     * Exception signalling that the requested demographic resource was not
     * found or parameters were invalid.
     */
    public static class DemographicsResourceNotFoundException extends RuntimeException {
        public DemographicsResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception signalling that the demographics BC is currently unavailable
     * or in an invalid state.
     */
    public static class DemographicsServiceUnavailableException extends RuntimeException {
        public DemographicsServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Generic exception for any other failure in the demographics ACL integration.
     */
    public static class DemographicsAccessException extends RuntimeException {
        public DemographicsAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}