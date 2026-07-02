package tech.nocountry.talent.appbitservice.telemetry.domain.model.queries;

import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.ClusterName;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.SessionPeriod;

import java.time.LocalDate;

/**
 * Query para buscar concentraciones por cluster y otros filtros.
 *
 * <p>Permite buscar métricas de concentración de red filtrando por:</p>
 * <ul>
 *   <li>Cluster geográfico</li>
 *   <li>Rango de fechas</li>
 *   <li>Período de sesión</li>
 * </ul>
 *
 * <p>Anteriormente conocido como GetConcentracionByClusterQuery.</p>
 */
public record GetConcentrationByClusterQuery(
        ClusterName cluster,
        LocalDate startDate,
        LocalDate endDate,
        SessionPeriod period
) {
    /**
     * Crea una query para buscar por cluster.
     *
     * @param cluster nombre del cluster
     * @return query con el filtro de cluster
     */
    public static GetConcentrationByClusterQuery byCluster(String cluster) {
        return new GetConcentrationByClusterQuery(
                cluster != null ? ClusterName.of(cluster) : null,
                null,
                null,
                null
        );
    }

    /**
     * Crea una query vacía para buscar todas las concentraciones.
     *
     * @return query sin filtros
     */
    public static GetConcentrationByClusterQuery all() {
        return new GetConcentrationByClusterQuery(null, null, null, null);
    }
}