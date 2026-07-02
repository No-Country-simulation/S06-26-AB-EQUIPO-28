package tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.entities.NetworkConcentration;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.queries.GetConcentrationByClusterQuery;
import tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories.ConcentrationRepository;

import java.util.List;

/**
 * Caso de uso para obtener métricas de concentración por cluster geográfico.
 *
 * <p>Utiliza el repositorio directamente para evitar dependencias circulares.</p>
 *
 * <p>Anteriormente conocido como GetConcentracionByClusterUseCase.</p>
 */
@Service
@RequiredArgsConstructor
public class GetConcentrationByClusterQueryUseCase {
    private final ConcentrationRepository concentrationRepository;

    /**
     * Maneja la consulta para obtener concentración por cluster.
     *
     * @param query query con los criterios de búsqueda
     * @return lista de métricas de concentración
     */
    @Transactional(readOnly = true)
    public List<NetworkConcentration> handle(GetConcentrationByClusterQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("query is required");
        }

        // If cluster is specified, filter by cluster
        if (query.cluster() != null) {
            var concentrations = concentrationRepository.findByCluster(query.cluster());

            // Apply additional filters if specified
            return applyFilters(concentrations, query);
        }

        // Otherwise, return all if no filters (or implement pagination)
        return concentrationRepository.findAll();
    }

    private List<NetworkConcentration> applyFilters(
            List<NetworkConcentration> concentrations,
            GetConcentrationByClusterQuery query
    ) {
        var result = concentrations;

        // Filter by date range
        if (query.startDate() != null) {
            result = result.stream()
                    .filter(c -> !c.getDayDate().isBefore(query.startDate()))
                    .toList();
        }

        if (query.endDate() != null) {
            result = result.stream()
                    .filter(c -> !c.getDayDate().isAfter(query.endDate()))
                    .toList();
        }

        // Filter by period
        if (query.period() != null) {
            result = result.stream()
                    .filter(c -> c.getSessionPeriod() == query.period())
                    .toList();
        }

        return result;
    }
}