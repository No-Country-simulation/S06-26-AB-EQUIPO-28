package tech.nocountry.talent.appbitservice.telemetry.domain.exceptions;

/**
 * Excepción base abstracta para el dominio de telemetría.
 *
 * <p>Todas las excepciones de dominio de telemetría extienden esta clase.
 * La robustez está en las excepciones específicas, no en una base compleja.</p>
 */
public abstract class TelemetryDomainException extends RuntimeException {
    protected TelemetryDomainException(String message) {
        super(message);
    }

    protected TelemetryDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}