package tech.nocountry.talent.appbitservice.employability.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando no se encuentra un registro de tiempo de viaje
 * inter-cluster (travel time) por sus clusters de origen y destino.
 */
public class TravelTimeNotFoundException extends EmployabilityDomainException {

    /**
     * Construye la excepción indicando los clusters de origen y destino no encontrados.
     *
     * @param origin      cluster de origen
     * @param destination cluster de destino
     */
    public TravelTimeNotFoundException(String origin, String destination) {
        super(ErrorCodes.EMP_OD_PAIR_NOT_FOUND,
                String.format("Travel time not found: %s -> %s", origin, destination));
    }

    /**
     * Construye la excepción con origen, destino y causa raíz.
     *
     * @param origin      cluster de origen
     * @param destination cluster de destino
     * @param cause       causa raíz
     */
    public TravelTimeNotFoundException(String origin, String destination, Throwable cause) {
        super(ErrorCodes.EMP_OD_PAIR_NOT_FOUND,
                String.format("Travel time not found: %s -> %s", origin, destination),
                cause);
    }
}