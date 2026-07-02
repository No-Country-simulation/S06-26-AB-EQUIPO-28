package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.ConnectivityLevel;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.RegionMetric;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.VulnerabilityScore;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.transform.TelemetryErrorAssembler;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.transform.TelemetryExternalResourceAssembler;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.acl.telemetry.TelemetryForInclusionAclAdapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ACL Facade para el contexto de Telemetría.
 *
 * <p>Simplifica la interacción con el Bounded Context de Telemetría.
 * Es el límite físico del ACL y el orquestador principal. Aísla el código complejo
 * y delega el transporte al Adapter.</p>
 *
 * <p>Implementa {@link TelemetryAclPort} y es inyectada en los casos de uso de inclusion-core.</p>
 *
 * <p>USA INYECCIÓN DIRECTA del TelemetryForInclusionAclAdapter (no HTTP interno)
 * siguiendo el patrón de Gastro Suite para monoliths modulares.</p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TelemetryAclFacade implements TelemetryAclPort {
    private final TelemetryForInclusionAclAdapter adapter;
    private final TelemetryExternalResourceAssembler assembler;

    @Override
    public List<RegionMetric> getConcentrationByCluster(String cluster) {
        try {
            log.info("Obteniendo concentration por cluster: {} (inyección directa)", cluster);
            var upstreamResources = adapter.findConcentrationsByCluster(cluster);
            return assembler.toRegionMetricListFromUpstream(upstreamResources);
        } catch (Exception ex) {
            throw TelemetryErrorAssembler.assemble(ex, "cluster: " + cluster);
        }
    }

    @Override
    public List<RegionMetric> getAllConcentration() {
        try {
            log.info("Obteniendo todas las concentrations con cluster info (inyección directa)");

            // Get antenna ECGI -> cluster mapping
            var antennas = adapter.findAllAntennas();
            Map<String, String> ecgiToCluster = antennas.stream()
                    .collect(Collectors.toMap(
                            tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource::ecgi,
                            tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource::cluster,
                            (existing, replacement) -> existing  // keep first if duplicates
                    ));

            // Get all concentrations
            var concentrations = adapter.findAllConcentrations();

            // Map to RegionMetric with cluster name instead of ECGI
            return concentrations.stream()
                    .map(resource -> {
                        String clusterName = ecgiToCluster.getOrDefault(resource.ecgi(), resource.ecgi());
                        ConnectivityLevel connectivity = ConnectivityLevel.fromDropPercentage(resource.dropPct());
                        return RegionMetric.of(
                                clusterName,
                                VulnerabilityScore.of(0),
                                connectivity,
                                resource.userCount(),
                                resource.dropPct(),
                                resource.congestionLevel()
                        );
                    })
                    .toList();
        } catch (Exception ex) {
            throw TelemetryErrorAssembler.assemble(ex, "all-concentration");
        }
    }
}
