package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

/**
 * Ensamblador de errores para el ACL de Demografía.
 *
 * <p>Intercepta excepciones técnicas del upstream y las transforma en excepciones
 * de dominio apropiadas, preservando la causa raíz para facilitar el debugging.</p>
 */
public final class DemographicsErrorAssembler {
    private static final Logger log = LoggerFactory.getLogger(DemographicsErrorAssembler.class);

    private DemographicsErrorAssembler() { }

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
        log.error("Error en comunicación con Demographics BC: {}", contextMessage, cause);

        String errorMessage = String.format("Error al obtener datos demográficos: %s", contextMessage);

        if (cause instanceof RestClientException restClientException) {
            if (restClientException.getMessage() != null &&
                restClientException.getMessage().contains("404")) {
                return new DemographicsResourceNotFoundException(
                        String.format("Recurso demográfico no encontrado: %s", contextMessage),
                        cause
                );
            }
            if (restClientException.getMessage() != null &&
                restClientException.getMessage().contains("timeout")) {
                return new DemographicsServiceUnavailableException(
                        String.format("Servicio de demografía no disponible: %s", contextMessage),
                        cause
                );
            }
        }

        return new DemographicsAccessException(errorMessage, cause);
    }

    /**
     * Excepción de dominio: recurso demográfico no encontrado.
     */
    public static class DemographicsResourceNotFoundException extends RuntimeException {
        public DemographicsResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Excepción de dominio: servicio de demografía no disponible.
     */
    public static class DemographicsServiceUnavailableException extends RuntimeException {
        public DemographicsServiceUnavailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Excepción de dominio: error genérico al acceder a datos demográficos.
     */
    public static class DemographicsAccessException extends RuntimeException {
        public DemographicsAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}