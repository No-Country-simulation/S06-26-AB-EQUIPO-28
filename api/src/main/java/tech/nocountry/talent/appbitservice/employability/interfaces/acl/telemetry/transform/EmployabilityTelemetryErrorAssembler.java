package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform;

import lombok.extern.slf4j.Slf4j;

/**
 * Error assembler for the Telemetry ACL — employability side.
 *
 * <p>Intercepts exceptions raised by the in-process telemetry integration and
 * translates them into domain-appropriate exceptions for the employability BC,
 * preserving the root cause for debugging.</p>
 *
 * <p>Follows the same structure as
 * {@code mentorship.interfaces.acl.inclusioncore.transform.InclusionCoreErrorAssembler}:
 * since this is an in-process (non-HTTP) integration, the discriminator is the
 * exception type rather than HTTP status codes.</p>
 */
@Slf4j
public final class EmployabilityTelemetryErrorAssembler {

    private EmployabilityTelemetryErrorAssembler() {
    }

    /**
     * Translates a technical exception into a domain-appropriate exception for
     * the employability BC.
     *
     * @param cause   the original exception from the telemetry integration
     * @param context human-readable context for the error message
     * @return a RuntimeException subclass appropriate to the error type
     */
    public static RuntimeException assemble(Exception cause, String context) {
        log.error("Error en comunicación con Telemetry BC: {}", context, cause);

        String message = String.format("Error al obtener datos de telemetría para employability: %s", context);

        if (cause instanceof IllegalArgumentException) {
            return new TelemetryResourceNotFoundException(
                    String.format("Parámetros inválidos para telemetry: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        if (cause instanceof IllegalStateException) {
            return new TelemetryServiceUnavailableException(
                    String.format("Telemetry BC no disponible: %s — %s", context, cause.getMessage()),
                    cause
            );
        }

        return new TelemetryAccessException(message, cause);
    }

    /**
     * Exception signalling that the requested telemetry resource was not
     * found or parameters were invalid.
     */
    public static class TelemetryResourceNotFoundException extends RuntimeException {
        public TelemetryResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception signalling that the telemetry BC is currently unavailable or
     * in an invalid state.
     */
    public static class TelemetryServiceUnavailableException extends RuntimeException {
        public TelemetryServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Generic exception for any other failure in the telemetry ACL integration.
     */
    public static class TelemetryAccessException extends RuntimeException {
        public TelemetryAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}