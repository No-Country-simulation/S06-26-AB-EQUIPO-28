package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.transform;

import lombok.extern.slf4j.Slf4j;

/**
 * Error assembler for the Inclusion Core ACL — mentorship side.
 *
 * <p>Intercepts exceptions from the in-process inclusioncore integration
 * and translates them into domain-appropriate exceptions for the mentorship BC,
 * preserving the root cause for debugging.</p>
 *
 * <p>Follows the same structure as
 * {@code inclusioncore.interfaces.acl.telemetry.transform.TelemetryErrorAssembler}.</p>
 */
@Slf4j
public final class InclusionCoreErrorAssembler {

    private InclusionCoreErrorAssembler() {
    }

    /**
     * Translates a technical exception into a domain-appropriate exception
     * for the mentorship BC.
     *
     * @param cause   the original exception from the inclusioncore integration
     * @param context human-readable context for the error message
     * @return a RuntimeException subclass appropriate to the error type
     */
    public static RuntimeException assemble(Exception cause, String context) {
        log.error("Error en comunicación con InclusionCore BC: {}", context, cause);

        String message = String.format("Error al obtener datos de inclusioncore para gap analysis: %s", context);

        if (cause instanceof IllegalArgumentException) {
            return new InclusionCoreResourceNotFoundException(
                    String.format("Parámetros inválidos para inclusioncore: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        if (cause instanceof IllegalStateException) {
            return new InclusionCoreServiceUnavailableException(
                    String.format("InclusionCore BC no disponible: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        return new InclusionCoreAccessException(message, cause);
    }

    /**
     * Exception signalling that the requested resource (region data) was not
     * found or parameters were invalid.
     */
    public static class InclusionCoreResourceNotFoundException extends RuntimeException {
        public InclusionCoreResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception signalling that the inclusioncore BC is currently unavailable
     * or in an invalid state.
     */
    public static class InclusionCoreServiceUnavailableException extends RuntimeException {
        public InclusionCoreServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Generic exception for any other failure in the inclusioncore ACL integration.
     */
    public static class InclusionCoreAccessException extends RuntimeException {
        public InclusionCoreAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}