package tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.MobilityODPair;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.TravelTime;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.EmployabilityGap;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GapSeverity;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetEmployabilityGapsQuery;
import tech.nocountry.talent.appbitservice.employability.domain.services.EmployabilityGapCalculator;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.MobilityODPairRepository;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.TravelTimeRepository;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.EmployabilityClusterCitizenSummary;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.EmployabilityDemographicsAclPort;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.EmployabilityClusterTelemetrySummary;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.EmployabilityTelemetryAclPort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Atomic query use case (CORE) que analiza brechas de empleabilidad por cluster
 * geográfico cruzando la matriz OD de movilidad de este BC con los índices
 * demográficos y de telemetría obtenidos vía ACLs.
 *
 * <p><b>Algoritmo</b>:</p>
 * <ol>
 *   <li>Obtiene todos los clusters demográficos (demanda: ciudadanos).</li>
 *   <li>Obtiene los clusters con antena (cobertura de telemetría).</li>
 *   <li>Obtiene la densidad diurna promedio por cluster (telemetría).</li>
 *   <li>Obtiene income C+D y jóvenes 18-24 por cluster (demografía).</li>
 *   <li>Calcula los TOP-5 hubs por volumen OD entrante (sum uniqueUsers por
 *       destinationCluster, orden desc).</li>
 *   <li>Por cada cluster demográfico, ensambla las señales crudas: cobertura,
 *       densidad diurna, viajes salientes a hubs, distancia al hub más cercano,
 *       municipios e intensidad de movilidad.</li>
 *   <li>Calcula los cuartiles (Q1) de densidad diurna (solo clusters CON
 *       telemetría) y de viajes salientes (todos los clusters).</li>
 *   <li>Invoca {@link EmployabilityGapCalculator#calculate} con los datos y los
 *       umbrales dinámicos.</li>
 *   <li>Aplica filtros de la query (minSeverity, cluster, onlyBlindZones),
 *       ordena por gapScore desc y limita a maxResults.</li>
 * </ol>
 *
 * <p>Es {@code @Transactional(readOnly = true)} porque carga los agregados de
 * movilidad desde la BD de este BC; los datos demográficos y de telemetría
 * provienen de los ACLs (no transaccionales contra este contexto).</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetEmployabilityGapsUseCase {

    /** Número de hubs de empleo considerados al calcular viajes salientes y distancia. */
    private static final int TOP_HUBS_LIMIT = 5;

    private final EmployabilityDemographicsAclPort demographicsAclPort;
    private final EmployabilityTelemetryAclPort telemetryAclPort;
    private final MobilityODPairRepository mobilityODPairRepository;
    private final TravelTimeRepository travelTimeRepository;

    /**
     * Ejecuta el análisis de brechas de empleabilidad.
     *
     * @param query la query con maxResults, minSeverity, cluster y onlyBlindZones
     * @return lista de {@link EmployabilityGap} ordenada por gapScore desc
     */
    public List<EmployabilityGap> execute(GetEmployabilityGapsQuery query) {
        // --- Paso 1: datos demográficos (demanda) ---
        List<EmployabilityClusterCitizenSummary> citizenByCluster = demographicsAclPort.getCitizenCountByCluster();

        // --- Paso 2: clusters con antena (cobertura de telemetría) ---
        Set<String> antennaClusters = telemetryAclPort.getAntennaClusters();

        // --- Paso 3: densidad diurna por cluster ---
        List<EmployabilityClusterTelemetrySummary> daytimeByCluster = telemetryAclPort.getDaytimeAvgUsersByCluster();
        Map<String, Double> daytimeByClusterMap = daytimeByCluster.stream()
                .collect(Collectors.toMap(
                        EmployabilityClusterTelemetrySummary::clusterName,
                        EmployabilityClusterTelemetrySummary::avgUsers,
                        (a, b) -> a));

        // --- Paso 4: income C+D por cluster ---
        Map<String, Long> incomeDByCluster = toCountMap(
                demographicsAclPort.getCitizenCountByClusterAndIncomeLevel("D"));
        Map<String, Long> incomeCByCluster = toCountMap(
                demographicsAclPort.getCitizenCountByClusterAndIncomeLevel("C"));

        // --- Paso 5: jóvenes 18-24 por cluster ---
        Map<String, Long> youthByCluster = toCountMap(
                demographicsAclPort.getCitizenCountByClusterAndAgeGroups(List.of("18-24")));

        // --- Paso 6: cargar matriz OD y tiempos de viaje (este BC) ---
        List<MobilityODPair> odPairs = mobilityODPairRepository.findAll();
        List<TravelTime> travelTimes = travelTimeRepository.findAll();

        // TOP-5 hubs por volumen OD entrante (destinationCluster).
        Set<String> topHubs = computeTopHubs(odPairs);

        // --- Pasos 7-9: ensamblar señales por cluster y calcular la brecha ---
        // Primero recolectamos las señales crudas para poder calcular los
        // cuartiles (que requieren el universo completo) antes de invocar al
        // calculator.
        List<ClusterSignals> allSignals = new ArrayList<>();
        for (EmployabilityClusterCitizenSummary citizenSummary : citizenByCluster) {
            String cluster = citizenSummary.clusterName();
            ClusterSignals signals = buildSignals(
                    cluster, citizenSummary, antennaClusters, daytimeByClusterMap,
                    incomeDByCluster, incomeCByCluster, youthByCluster,
                    odPairs, travelTimes, topHubs);
            allSignals.add(signals);
        }

        // --- Paso 8: cuartiles ---
        double daytimeQ1 = percentile(
                allSignals.stream()
                        .filter(s -> s.hasTelemetryCoverage)
                        .mapToDouble(s -> s.daytimeAvgUsers)
                        .sorted().toArray(),
                25.0);
        double outboundQ1 = percentile(
                allSignals.stream()
                        .mapToLong(s -> s.outboundTripsToHubs)
                        .sorted().toArray(),
                25.0);

        // --- Paso 9: calcular la brecha por cluster ---
        List<EmployabilityGap> gaps = new ArrayList<>();
        for (ClusterSignals s : allSignals) {
            EmployabilityGap gap = EmployabilityGapCalculator.calculate(
                    s.cluster,
                    s.municipalities,
                    s.citizenCount,
                    s.incomeDCount,
                    s.incomeCCount,
                    s.youthCount18_24,
                    s.hasTelemetryCoverage,
                    s.daytimeAvgUsers,
                    s.outboundTripsToHubs,
                    s.distanceToNearestHubKm,
                    s.mobilityIntensity,
                    daytimeQ1,
                    outboundQ1
            );
            gaps.add(gap);
        }

        // --- Pasos 10-13: filtros, orden y límite ---
        List<EmployabilityGap> filtered = gaps.stream()
                .filter(g -> matchesMinSeverity(g, query.minSeverity()))
                .filter(g -> matchesCluster(g, query.cluster()))
                .filter(g -> matchesOnlyBlindZones(g, query.onlyBlindZones()))
                .sorted(Comparator.comparingDouble(EmployabilityGap::gapScore).reversed())
                .limit(query.maxResults())
                .toList();

        return filtered;
    }

    /**
     * Ensambla las señales crudas de un cluster a partir de las fuentes de
     * demografía, telemetría y movilidad.
     */
    private ClusterSignals buildSignals(
            String cluster,
            EmployabilityClusterCitizenSummary citizenSummary,
            Set<String> antennaClusters,
            Map<String, Double> daytimeByClusterMap,
            Map<String, Long> incomeDByCluster,
            Map<String, Long> incomeCByCluster,
            Map<String, Long> youthByCluster,
            List<MobilityODPair> odPairs,
            List<TravelTime> travelTimes,
            Set<String> topHubs) {

        boolean hasTelemetryCoverage = antennaClusters.contains(cluster);
        double daytimeAvgUsers = daytimeByClusterMap.getOrDefault(cluster, 0.0);
        long incomeDCount = incomeDByCluster.getOrDefault(cluster, 0L);
        long incomeCCount = incomeCByCluster.getOrDefault(cluster, 0L);
        long youthCount18_24 = youthByCluster.getOrDefault(cluster, 0L);

        // OD pairs cuyo origen es este cluster y destino es un hub TOP-5.
        List<MobilityODPair> outboundToHubs = odPairs.stream()
                .filter(od -> Objects.equals(od.getOriginCluster(), cluster)
                        && topHubs.contains(od.getDestinationCluster()))
                .toList();

        // Viajes salientes a hubs = suma de uniqueUsers.
        long outboundTripsToHubs = outboundToHubs.stream()
                .mapToLong(MobilityODPair::getUniqueUsers)
                .sum();

        // Distancia al hub más cercano: mínima avgDistanceKm de los OD directos
        // a hubs; si no hay OD directo, cae a TravelTime.
        double distanceToNearestHubKm = outboundToHubs.stream()
                .mapToDouble(MobilityODPair::getAverageDistanceKm)
                .min()
                .orElseGet(() -> travelTimes.stream()
                        .filter(tt -> Objects.equals(tt.getOriginCluster(), cluster)
                                && topHubs.contains(tt.getDestinationCluster()))
                        .mapToDouble(TravelTime::getAverageDistanceKm)
                        .min()
                        .orElse(0.0));

        // Municipios: distinct originMunicipio de los OD con origen en el cluster.
        List<String> municipalities = odPairs.stream()
                .filter(od -> Objects.equals(od.getOriginCluster(), cluster))
                .map(MobilityODPair::getOriginMunicipio)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        // Intensidad de movilidad: no tenemos un mobility_pattern directo.
        // Usamos el predominantPeriod del primer OD saliente del cluster, o
        // "UNKNOWN" si no hay data.
        String mobilityIntensity = odPairs.stream()
                .filter(od -> Objects.equals(od.getOriginCluster(), cluster))
                .map(MobilityODPair::getPredominantPeriod)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("UNKNOWN");

        return new ClusterSignals(
                cluster,
                citizenSummary.citizenCount(),
                incomeDCount,
                incomeCCount,
                youthCount18_24,
                hasTelemetryCoverage,
                daytimeAvgUsers,
                outboundTripsToHubs,
                distanceToNearestHubKm,
                municipalities,
                mobilityIntensity
        );
    }

    /**
     * Calcula los TOP-5 hubs por volumen OD entrante: agrupa por
     * destinationCluster sumando uniqueUsers, ordena desc y toma los 5 primeros.
     */
    private Set<String> computeTopHubs(List<MobilityODPair> odPairs) {
        Map<String, Long> incomingVolume = new HashMap<>();
        for (MobilityODPair od : odPairs) {
            String dest = od.getDestinationCluster();
            if (dest == null) continue;
            incomingVolume.merge(dest, (long) od.getUniqueUsers(), Long::sum);
        }
        return incomingVolume.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(TOP_HUBS_LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Convierte una lista de {@link EmployabilityClusterCitizenSummary} a un mapa
     * clusterName → citizenCount.
     */
    private Map<String, Long> toCountMap(List<EmployabilityClusterCitizenSummary> summaries) {
        Map<String, Long> map = new HashMap<>();
        for (EmployabilityClusterCitizenSummary s : summaries) {
            map.merge(s.clusterName(), s.citizenCount(), Long::sum);
        }
        return map;
    }

    /**
     * Calcula un percentil sobre un array ya ordenado, usando interpolación
     * lineal (método nearest-rank de la NASM simplificado). Devuelve 0.0 si el
     * array está vacío.
     *
     * @param sorted valores ordenados ascendentemente
     * @param p      percentil a calcular (0-100)
     * @return el valor del percentil
     */
    private double percentile(long[] sorted, double p) {
        if (sorted.length == 0) return 0.0;
        double[] d = new double[sorted.length];
        for (int i = 0; i < sorted.length; i++) d[i] = (double) sorted[i];
        return percentile(d, p);
    }

    private double percentile(double[] sorted, double p) {
        if (sorted.length == 0) return 0.0;
        if (sorted.length == 1) return sorted[0];
        double rank = (p / 100.0) * (sorted.length - 1);
        int lower = (int) Math.floor(rank);
        int upper = (int) Math.ceil(rank);
        if (lower == upper) return sorted[lower];
        double weight = rank - lower;
        return sorted[lower] * (1 - weight) + sorted[upper] * weight;
    }

    /**
     * Filtra por severidad mínima: mantiene el gap si su severidad es >= minSeverity
     * en el orden del enum {@link GapSeverity}.
     */
    private boolean matchesMinSeverity(EmployabilityGap gap, String minSeverity) {
        if (minSeverity == null || minSeverity.isBlank()) return true;
        GapSeverity min;
        try {
            min = GapSeverity.valueOf(minSeverity.toUpperCase().trim());
        } catch (IllegalArgumentException ex) {
            return true; // severidad inválida = no filtrar
        }
        GapSeverity current = GapSeverity.valueOf(gap.gapSeverity());
        // Orden del enum: CRITICAL > HIGH > MODERATE > LOW > NONE (ordinal 0..4).
        return current.ordinal() <= min.ordinal();
    }

    private boolean matchesCluster(EmployabilityGap gap, String cluster) {
        if (cluster == null || cluster.isBlank()) return true;
        return Objects.equals(gap.cluster(), cluster.trim());
    }

    private boolean matchesOnlyBlindZones(EmployabilityGap gap, boolean onlyBlindZones) {
        if (!onlyBlindZones) return true;
        return !gap.hasTelemetryCoverage();
    }

    /**
     * Contenedor interno de señales crudas por cluster, previo al cálculo del
     * gap (necesario para calcular los cuartiles sobre el universo completo).
     */
    private record ClusterSignals(
            String cluster,
            long citizenCount,
            long incomeDCount,
            long incomeCCount,
            long youthCount18_24,
            boolean hasTelemetryCoverage,
            double daytimeAvgUsers,
            long outboundTripsToHubs,
            double distanceToNearestHubKm,
            List<String> municipalities,
            String mobilityIntensity
    ) {}
}
