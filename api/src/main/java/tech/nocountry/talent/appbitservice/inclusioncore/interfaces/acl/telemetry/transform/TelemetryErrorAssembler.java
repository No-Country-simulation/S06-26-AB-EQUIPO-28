package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.transform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;

/**
 * Ensamblador de errores para el ACL de Telemetría.
 *
 * <p>Intercepta excepciones técnicas del upstream y las transforma en excepciones
 * de dominio apropiadas, preservando la causa raíz para facilitar el debugging.</p>
 */
@Slf4j
public final class TelemetryErrorAssembler {
    private TelemetryErrorAssembler() { }

    /**
     * Ensambla una excepción técnica en una excepción de dominio.
     *
     * <p>Preserva el stack trace original para facilitar el debugging.</p>
     *
     * @param cause la excepción técnica original
     * @param contextMessage mensaje de contexto adicional
     * @return la excepción de dominio apropiada
     */
    public static RuntimeException assemble(Exception cause, String contextMessage) {
        log.error("Error en comunicación con Telemetry BC: {}", contextMessage, cause);

        String errorMessage = String.format("Error al obtener datos de telemetría: %s", contextMessage);

        if (cause instanceof RestClientException restClientException) {
            if (restClientException.getMessage() != null &&
                restClientException.getMessage().contains("404")) {
                return new TelemetryResourceNotFoundException(
                        String.format("Recurso de telemetría no encontrado: %s", contextMessage),
                        cause
                );
            }
            if (restClientException.getMessage() != null &&
                restClientException.getMessage().contains("timeout")) {
                return new TelemetryServiceUnavailableException(
                        String.format("Servicio de telemetría no disponible: %s", contextMessage),
                        cause
                );
            }
        }

        return new TelemetryAccessException(errorMessage, cause);
    }

    /**
     * Excepción de dominio: recurso de telemetría no encontrado.
     */
    public static class TelemetryResourceNotFoundException extends RuntimeException {
        public TelemetryResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Excepción de dominio: servicio de telemetría no disponible.
     */
    public static class TelemetryServiceUnavailableException extends RuntimeException {
        public TelemetryServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Excepción de dominio: error genérico al acceder a datos de telemetría.
     */
    public static class TelemetryAccessException extends RuntimeException {
        public TelemetryAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}