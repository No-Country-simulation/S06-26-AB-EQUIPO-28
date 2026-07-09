package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry;

/**
 * ACL result representing aggregated daytime telemetry for a cluster, expressed
 * in the employability bounded context's ubiquitous language.
 *
 * <p>This is the anti-corruption translation of the upstream concentration +
 * antenna data from the telemetry BC. The employability BC never sees the
 * upstream resource shapes: it only interacts with this record.</p>
 *
 * @param clusterName       the geographic cluster name
 * @param avgUsers          the average user count across daytime (MORNING + AFTERNOON) measurements
 * @param measurementCount  the number of daytime concentration records aggregated for the cluster
 */
public record EmployabilityClusterTelemetrySummary(
        String clusterName,
        double avgUsers,
        int measurementCount
) {}