package tech.nocountry.talent.appbitservice.telemetry.domain.exceptions;

/**
 * Excepción lanzada cuando no se encuentra una antenna por su ECGI.
 */
public class AntennaNotFoundException extends TelemetryDomainException {
    public AntennaNotFoundException(String ecgi) {
        super(String.format("La antenna con ECGI %s no fue encontrada", ecgi));
    }

    public AntennaNotFoundException(String ecgi, String message) {
        super(String.format("La antenna con ECGI %s no fue encontrada. %s", ecgi, message));
    }

    public AntennaNotFoundException(String ecgi, Throwable cause) {
        super(String.format("La antenna con ECGI %s no fue encontrada", ecgi), cause);
    }
}