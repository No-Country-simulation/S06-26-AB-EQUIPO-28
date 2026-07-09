package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.adapter.EmployabilityTelemetryInternalClient;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform.EmployabilityTelemetryErrorAssembler;

import java.util.List;
import java.util.Set;

/**
 * ACL Facade for the Telemetry bounded context — employability side.
 *
 * <p>Implements {@link EmployabilityTelemetryAclPort} and is the physical boundary of the
 * ACL. It orchestrates the {@link EmployabilityTelemetryInternalClient} (transport layer,
 * direct in-process injection, no HTTP) and translates any failure into a
 * domain-appropriate exception via {@link EmployabilityTelemetryErrorAssembler}.</p>
 *
 * <p>This facade is the only entry point the employability use cases use to
 * reach telemetry data, keeping the upstream model out of the employability
 * domain.</p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EmployabilityTelemetryAclFacade implements EmployabilityTelemetryAclPort {

    private final EmployabilityTelemetryInternalClient adapter;

    @Override
    public List<EmployabilityClusterTelemetrySummary> getDaytimeAvgUsersByCluster() {
        try {
            log.info("Fetching daytime average users by cluster");
            return adapter.getDaytimeAvgUsersByCluster();
        } catch (Exception ex) {
            throw EmployabilityTelemetryErrorAssembler.assemble(ex, "daytime-avg-users-by-cluster");
        }
    }

    @Override
    public Set<String> getAntennaClusters() {
        try {
            log.info("Fetching antenna clusters");
            return adapter.getAntennaClusters();
        } catch (Exception ex) {
            throw EmployabilityTelemetryErrorAssembler.assemble(ex, "antenna-clusters");
        }
    }
}