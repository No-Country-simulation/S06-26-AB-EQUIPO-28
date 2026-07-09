package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry;

import java.util.List;
import java.util.Set;

/**
 * ACL Port for the Telemetry bounded context — employability side.
 *
 * <p>This interface is consumed by the employability use cases to obtain
 * daytime network concentration data (average users per cluster during
 * business hours) and the set of known antenna clusters. These feed the
 * employability-gap analysis: a cluster with high daytime concentration but
 * low formal employment signals an employability gap.</p>
 *
 * <p>The employability BC depends ONLY on this interface, never directly on the
 * telemetry model. It speaks only the employability ubiquitous language via
 * {@link EmployabilityClusterTelemetrySummary}. Implementation: {@link EmployabilityTelemetryAclFacade}.</p>
 */
public interface EmployabilityTelemetryAclPort {

    /**
     * Gets the average daytime user count grouped by cluster.
     *
     * <p>"Daytime" is defined as the {@code MORNING} and {@code AFTERNOON}
     * session periods of the telemetry BC. The aggregation joins each
     * concentration record to its antenna's cluster via the
     * {@code ecgi -> cluster} mapping.</p>
     *
     * @return list of cluster telemetry summaries
     */
    List<EmployabilityClusterTelemetrySummary> getDaytimeAvgUsersByCluster();

    /**
     * Gets the set of distinct geographic clusters covered by the antennas.
     *
     * @return set of unique cluster names
     */
    Set<String> getAntennaClusters();
}