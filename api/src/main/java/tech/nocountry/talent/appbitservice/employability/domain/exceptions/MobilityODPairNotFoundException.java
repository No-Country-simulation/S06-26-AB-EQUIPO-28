package tech.nocountry.talent.appbitservice.employability.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando no se encuentra un par origen-destino de movilidad
 * (mobility OD pair) por sus clusters de origen y destino.
 */
public class MobilityODPairNotFoundException extends EmployabilityDomainException {

    /**
     * Construye la excepción indicando los clusters de origen y destino no encontrados.
     *
     * @param origin      cluster de origen
     * @param destination cluster de destino
     */
    public MobilityODPairNotFoundException(String origin, String destination) {
        super(ErrorCodes.EMP_OD_PAIR_NOT_FOUND,
                String.format("Mobility OD pair not found: %s -> %s", origin, destination));
    }

    /**
     * Construye la excepción con origen, destino y causa raíz.
     *
     * @param origin      cluster de origen
     * @param destination cluster de destino
     * @param cause       causa raíz
     */
    public MobilityODPairNotFoundException(String origin, String destination, Throwable cause) {
        super(ErrorCodes.EMP_OD_PAIR_NOT_FOUND,
                String.format("Mobility OD pair not found: %s -> %s", origin, destination),
                cause);
    }
}