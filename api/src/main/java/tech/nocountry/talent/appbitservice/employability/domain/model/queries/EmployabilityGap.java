package tech.nocountry.talent.appbitservice.employability.domain.model.queries;

import java.util.List;

/**
 * Read model (NO entidad JPA) que representa el resultado del cálculo de una
 * brecha de empleabilidad para un cluster geográfico.
 *
 * <p>Es un DTO de dominio inmutable producido por {@link
 * tech.nocountry.talent.appbitservice.employability.domain.services.EmployabilityGapCalculator}
 * al cruzar la matriz OD de movilidad de este BC con los índices demográficos
 * del BC inclusion-core. Expone el puntaje agregado ({@code gapScore}), su
 * severidad derivada ({@code gapSeverity}) y la lista de factores primarios
 * que dispararon el cálculo, para trazabilidad explicativa hacia el gestor
 * público.</p>
 *
 * @param cluster               nombre del cluster geográfico
 * @param municipalities        municipios que componen el cluster
 * @param citizenCount          total de ciudadanos en el cluster
 * @param incomeDCount          ciudadanos en nivel de renta D (baja renta)
 * @param incomeCCount          ciudadanos en nivel de renta C
 * @param youthCount18_24       jóvenes de 18 a 24 años
 * @param hasTelemetryCoverage  {@code true} si hay cobertura de telemetría reportada
 * @param daytimeAvgUsers       promedio de usuarios activos en horario laboral
 * @param outboundTripsToHubs   viajes salientes hacia los hubs de empleo
 * @param distanceToNearestHubKm distancia en km al hub de empleo más cercano
 * @param mobilityIntensity     intensidad de movilidad (etiqueta descriptiva)
 * @param gapSeverity           severidad derivada del {@code gapScore}
 * @param gapScore              puntaje agregado de la brecha (0-100)
 * @param primaryFactors        lista de factores que dispararon el cálculo (explicativos)
 */
public record EmployabilityGap(
        String cluster,
        List<String> municipalities,
        long citizenCount,
        long incomeDCount,
        long incomeCCount,
        long youthCount18_24,
        boolean hasTelemetryCoverage,
        double daytimeAvgUsers,
        long outboundTripsToHubs,
        double distanceToNearestHubKm,
        String mobilityIntensity,
        String gapSeverity,
        double gapScore,
        List<String> primaryFactors
) {
}