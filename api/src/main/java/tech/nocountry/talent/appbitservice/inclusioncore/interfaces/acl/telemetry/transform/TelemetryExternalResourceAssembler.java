package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.ConnectivityLevel;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.RegionMetric;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.valueobjects.VulnerabilityScore;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.resources.TelemetryConcentrationResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Assembler para transformar recursos crudos del upstream Telemetry BC al modelo de dominio de inclusion-core.
 *
 * <p>Esta interfaz utiliza MapStruct para garantizar rendimiento ultrarrápido y seguridad de tipos
 * en tiempo de compilación. Transforma {@link TelemetryConcentrationResource} (modelo externo)
 * a {@link RegionMetric} (modelo del dominio de inclusion-core).</p>
 *
 * <p>También acepta directamente {@link ConcentrationResource} del upstream Telemetry BC
 * para evitar crear DTOs intermedios innecesarios en la inyección directa.</p>
 */
@Mapper(componentModel = "spring")
public interface TelemetryExternalResourceAssembler {
    /**
     * Transforma un recurso de concentración externo a una métrica de región.
     *
     * @param resource el recurso externo del upstream Telemetry BC
     * @return la métrica de región del dominio de inclusion-core
     */
    RegionMetric toRegionMetric(TelemetryConcentrationResource resource);

    /**
     * Transforma una lista de recursos de concentración externos a una lista de métricas de región.
     *
     * @param resources la lista de recursos externos
     * @return la lista de métricas de región del dominio de inclusion-core
     */
    List<RegionMetric> toRegionMetricList(List<TelemetryConcentrationResource> resources);

    /**
     * Transforma un recurso ConcentrationResource del upstream Telemetry BC directamente a RegionMetric.
     *
     * <p>Este método se utiliza cuando se usa inyección directa del ACL Adapter
     * en lugar de llamadas HTTP internas.</p>
     *
     * <p>Como ConcentrationResource no tiene todos los campos de RegionMetric,
     * se calculan los valores faltantes: vulnerabilityScore (0 por defecto),
     * connectivityLevel (derivado de dropPct).</p>
     *
     * @param resource el recurso del upstream Telemetry BC
     * @return la métrica de región del dominio de inclusion-core
     */
    default RegionMetric toRegionMetricFromUpstream(ConcentrationResource resource) {
        if (resource == null) {
            return null;
        }
        ConnectivityLevel connectivity = ConnectivityLevel.fromDropPercentage(resource.dropPct());
        return RegionMetric.of(
                resource.ecgi(),
                VulnerabilityScore.of(0),
                connectivity,
                resource.userCount(),
                resource.dropPct(),
                resource.congestionLevel()
        );
    }

    /**
     * Transforma una lista de ConcentrationResource del upstream a RegionMetric.
     *
     * @param resources la lista de recursos del upstream
     * @return la lista de métricas de región
     */
    default List<RegionMetric> toRegionMetricListFromUpstream(List<ConcentrationResource> resources) {
        if (resources == null) {
            return List.of();
        }
        return resources.stream()
                .map(this::toRegionMetricFromUpstream)
                .toList();
    }

    /**
     * Transforma un recurso externo a nivel de conectividad.
     *
     * @param resource el recurso externo
     * @return el nivel de conectividad
     */
    default ConnectivityLevel toConnectivityLevel(TelemetryConcentrationResource resource) {
        if (resource == null || resource.dropPct() == null) {
            return ConnectivityLevel.LOW;
        }
        return ConnectivityLevel.fromDropPercentage(resource.dropPct());
    }
}