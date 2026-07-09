package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.EmployabilityClusterTelemetrySummary;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources.EmployabilityAntennaRawResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources.EmployabilityConcentrationRawResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform.EmployabilityTelemetryExternalResourceAssembler;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna.GetAllAntennasEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration.GetConcentrationFilteredEndpoint;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter that consumes the telemetry BC internal endpoints via direct
 * in-process injection (Gastro Suite pattern for modular monoliths).
 *
 * <p>No HTTP, no REST clients: it injects the {@code @Component}s
 * {@link GetAllAntennasEndpoint} and {@link GetConcentrationFilteredEndpoint}
 * and, crucially, never touches the upstream telemetry resource shapes
 * directly. {@link EmployabilityTelemetryExternalResourceAssembler} (MapStruct) translates
 * the upstream resources into the employability-owned raw resources
 * ({@link EmployabilityAntennaRawResource}, {@link EmployabilityConcentrationRawResource}); this adapter
 * then aggregates those into the employability ubiquitous language
 * ({@link EmployabilityClusterTelemetrySummary}). This is the only place in the
 * employability BC that reaches into the telemetry internal endpoints;
 * everything upstream of this adapter only sees the employability ACL results.</p>
 *
 * <p>The concentration records are keyed by antenna {@code ecgi}, so the
 * adapter builds an {@code ecgi -> cluster} map from the antennas and uses it
 * to group concentrations by cluster. Daytime is defined as the
 * {@code MORNING} + {@code AFTERNOON} session periods, matched as plain
 * strings so the upstream {@code SessionPeriod} enum never leaks in.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmployabilityTelemetryInternalClient {

    /**
     * Daytime session period names, expressed as strings to avoid importing
     * the upstream {@code SessionPeriod} enum. The ACL owns the definition of
     * "daytime" in the employability language; the upstream enum is only ever
     * read through {@link EmployabilityTelemetryExternalResourceAssembler} when the raw
     * concentration resources are built.
     */
    private static final Set<String> DAYTIME_PERIODS = Set.of("MORNING", "AFTERNOON");

    private final GetAllAntennasEndpoint getAllAntennasEndpoint;
    private final GetConcentrationFilteredEndpoint getConcentrationFilteredEndpoint;
    private final EmployabilityTelemetryExternalResourceAssembler telemetryExternalResourceAssembler;

    /**
     * Gets the distinct set of geographic clusters covered by the antennas.
     *
     * @return set of unique cluster names
     */
    public Set<String> getAntennaClusters() {
        log.debug("Fetching all antennas to extract unique clusters");
        var antennas = telemetryExternalResourceAssembler.toAntennaRawList(
                getAllAntennasEndpoint.handleAll().content());
        return antennas.stream()
                .map(EmployabilityAntennaRawResource::cluster)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.toSet());
    }

    /**
     * Gets the average daytime (MORNING + AFTERNOON) user count grouped by
     * cluster.
     *
     * <p>The aggregation pipeline:</p>
     * <ol>
     *   <li>Load all antennas and translate them to local
     *       {@link EmployabilityAntennaRawResource}, then build {@code ecgi -> cluster} map.</li>
     *   <li>Load all concentration records and translate them to local
     *       {@link EmployabilityConcentrationRawResource} (upstream {@code SessionPeriod}
     *       flattened to {@code String}).</li>
     *   <li>Filter to daytime periods with a non-null user count.</li>
     *   <li>Resolve each record's cluster via the ecgi map; records whose ecgi
     *       has no matching antenna are skipped.</li>
     *   <li>Group by cluster and compute the average {@code userCount} and the
     *       measurement count per cluster.</li>
     * </ol>
     *
     * @return list of cluster telemetry summaries
     */
    public List<EmployabilityClusterTelemetrySummary> getDaytimeAvgUsersByCluster() {
        log.debug("Building ecgi->cluster map from antennas");
        Map<String, String> ecgiToCluster = telemetryExternalResourceAssembler
                .toAntennaRawList(getAllAntennasEndpoint.handleAll().content())
                .stream()
                .collect(Collectors.toMap(
                        EmployabilityAntennaRawResource::ecgi,
                        EmployabilityAntennaRawResource::cluster,
                        (existing, replacement) -> existing
                ));

        log.debug("Fetching all concentration records to filter daytime periods");
        List<EmployabilityConcentrationRawResource> concentrations = telemetryExternalResourceAssembler
                .toConcentrationRawList(getConcentrationFilteredEndpoint.handleAll().content());

        // Pre-filter daytime records with a usable cluster and user count.
        record DaytimePoint(String cluster, int userCount) {}

        List<DaytimePoint> daytimePoints = concentrations.stream()
                .filter(c -> c.period() != null && DAYTIME_PERIODS.contains(c.period()))
                .filter(c -> c.userCount() != null)
                .map(c -> new DaytimePoint(ecgiToCluster.get(c.ecgi()), c.userCount()))
                .filter(p -> p.cluster() != null)
                .toList();

        // Partial aggregation result: total user count and number of measurements.
        record Aggregation(int sum, int count) {}

        // Group by cluster, computing sum and count in a single pass via teeing.
        return daytimePoints.stream()
                .collect(Collectors.groupingBy(
                        DaytimePoint::cluster,
                        Collectors.teeing(
                                Collectors.summingInt(DaytimePoint::userCount),
                                Collectors.counting(),
                                (sum, count) -> new Aggregation(sum, count.intValue())
                        )
                ))
                .entrySet().stream()
                .map(e -> new EmployabilityClusterTelemetrySummary(
                        e.getKey(),
                        e.getValue().count() == 0 ? 0.0 : (double) e.getValue().sum() / e.getValue().count(),
                        e.getValue().count()
                ))
                .toList();
    }
}