package tech.nocountry.talent.appbitservice.employability.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando se intenta construir una referencia a un cluster
 * ({{@code ClusterRef}}) con un valor nulo, vacío o solo espacios.
 */
public class InvalidClusterException extends EmployabilityDomainException {

    /**
     * Construye la excepción indicando el valor de cluster inválido recibido.
     *
     * @param value el valor inválido (puede ser {@code "null"} si era nulo)
     */
    public InvalidClusterException(String value) {
        super(ErrorCodes.EMP_INVALID_CLUSTER,
                String.format("Invalid cluster reference: %s", value));
    }

    /**
     * Construye la excepción con el valor inválido y causa raíz.
     *
     * @param value el valor inválido
     * @param cause causa raíz
     */
    public InvalidClusterException(String value, Throwable cause) {
        super(ErrorCodes.EMP_INVALID_CLUSTER,
                String.format("Invalid cluster reference: %s", value),
                cause);
    }
}