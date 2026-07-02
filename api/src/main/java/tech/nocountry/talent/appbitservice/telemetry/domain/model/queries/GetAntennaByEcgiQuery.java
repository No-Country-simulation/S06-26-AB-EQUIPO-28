package tech.nocountry.talent.appbitservice.telemetry.domain.model.queries;

import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.Ecgi;

/**
 * Query para buscar una antenna por su ECGI.
 *
 * <p>Permite buscar una antenna específica utilizando su identificador único.</p>
 */
public record GetAntennaByEcgiQuery(Ecgi ecgi) {
    /**
     * Crea una query para buscar por ECGI.
     *
     * @param ecgiValue valor del ECGI
     * @return query con el filtro de ECGI
     */
    public static GetAntennaByEcgiQuery byEcgi(String ecgiValue) {
        return new GetAntennaByEcgiQuery(Ecgi.of(ecgiValue));
    }
}