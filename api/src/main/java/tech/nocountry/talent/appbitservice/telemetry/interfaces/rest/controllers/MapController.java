package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna.GetAllAntennasEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration.GetConcentrationFilteredEndpoint;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.MapDocs;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller that aggregates antenna, concentration, and connectivity data
 * by cluster/region for the map view.
 *
 * <p>Returns a simplified structure grouped by cluster with aggregated metrics
 * suitable for frontend map rendering.</p>
 */
@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
public class MapController implements MapDocs {

    private final GetAllAntennasEndpoint getAllAntennasEndpoint;
    private final GetConcentrationFilteredEndpoint getConcentrationFilteredEndpoint;

    /**
     * GET /api/v1/map/regions — Returns all antenna data aggregated by cluster with
     * concentration and connectivity metrics.
     *
     * <p>Response shape:
     * <pre>{@code
     * {
     *   "regions": [
     *     {
     *       "name": "Norte",
     *       "lat": -27.5917,
     *       "lng": -48.5588,
     *       "concentration": 1250,
     *       "connectivity": 85.5,
     *       "indicators": {
     *         "antennas": 12,
     *         "averageUsers": 104,
     *         "averageDropPct": 2.3,
     *         "averageCongestion": 0.15
     *       }
     *     }
     *   ]
     * }</pre>
     */
    @Override
    @GetMapping("/regions")
    public ResponseEntity<Map<String, Object>> getRegions() {
        // 1. Fetch all antennas (paginated)
        AntennaPaginatedResource antennasPage = getAllAntennasEndpoint.handleAll();
        List<AntennaResource> antennas = antennasPage.content();

        // 2. Fetch all concentration data
        ConcentrationPaginatedResource concentrationPage = getConcentrationFilteredEndpoint.handleAll();
        List<ConcentrationResource> concentrations = concentrationPage.content();

        // 3. Index antennas by ecgi for lookup
        Map<String, AntennaResource> antennaByEcgi = antennas.stream()
                .collect(Collectors.toMap(AntennaResource::ecgi, a -> a, (a, b) -> a));

        // 4. Group concentration records by cluster (via antenna ecgi → cluster)
        Map<String, List<ConcentrationResource>> concentrationsByCluster = concentrations.stream()
                .filter(c -> antennaByEcgi.containsKey(c.ecgi()))
                .collect(Collectors.groupingBy(c -> antennaByEcgi.get(c.ecgi()).cluster()));

        // 5. Group antennas by cluster
        Map<String, List<AntennaResource>> antennasByCluster = antennas.stream()
                .filter(a -> a.cluster() != null && !a.cluster().isBlank())
                .collect(Collectors.groupingBy(AntennaResource::cluster));

        // 6. Build aggregated response per cluster
        Set<String> allClusters = new TreeSet<>();
        allClusters.addAll(antennasByCluster.keySet());
        allClusters.addAll(concentrationsByCluster.keySet());

        List<Map<String, Object>> regions = new ArrayList<>();

        for (String cluster : allClusters) {
            List<AntennaResource> clusterAntennas = antennasByCluster.getOrDefault(cluster, List.of());
            List<ConcentrationResource> clusterConcentrations = concentrationsByCluster.getOrDefault(cluster, List.of());

            // Compute centroid from antennas
            double avgLat = clusterAntennas.stream()
                    .mapToDouble(AntennaResource::latitude)
                    .average().orElse(0.0);
            double avgLng = clusterAntennas.stream()
                    .mapToDouble(AntennaResource::longitude)
                    .average().orElse(0.0);

            // Aggregate concentration metrics
            double avgUserCount = clusterConcentrations.stream()
                    .mapToInt(c -> c.userCount() != null ? c.userCount() : 0)
                    .average().orElse(0.0);
            double avgDropPct = clusterConcentrations.stream()
                    .mapToDouble(c -> c.dropPct() != null ? c.dropPct() : 0.0)
                    .average().orElse(0.0);
            double avgCongestion = clusterConcentrations.stream()
                    .mapToDouble(c -> c.congestionLevel() != null ? c.congestionLevel() : 0.0)
                    .average().orElse(0.0);

            // Coverage = inverse of drop percentage (higher drop = worse coverage)
            double coberturaRed = Math.max(0, 100.0 - avgDropPct);

            Map<String, Object> indicators = new LinkedHashMap<>();
            indicators.put("antennas", clusterAntennas.size());
            indicators.put("averageUsers", Math.round(avgUserCount));
            indicators.put("averageDropPct", Math.round(avgDropPct * 100.0) / 100.0);
            indicators.put("averageCongestion", Math.round(avgCongestion * 100.0) / 100.0);

            Map<String, Object> region = new LinkedHashMap<>();
            region.put("name", cluster);
            region.put("lat", Math.round(avgLat * 10000.0) / 10000.0);
            region.put("lng", Math.round(avgLng * 10000.0) / 10000.0);
            region.put("concentration", clusterConcentrations.size());
            region.put("connectivity", Math.round(coberturaRed * 100.0) / 100.0);
            region.put("indicators", indicators);

            regions.add(region);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("regions", regions);

        return ResponseEntity.ok(response);
    }
}
