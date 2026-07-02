package tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates.Antenna;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.entities.NetworkConcentration;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.queries.GetConcentrationFilteredQuery;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.SessionPeriod;
import tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories.ConcentrationRepository;

import java.util.ArrayList;

/**
 * Caso de uso para obtener métricas de concentración con filtros dinámicos y paginación.
 *
 * <p>Construye una {@link Specification} JPA a partir de los filtros opcionales del query
 * ({@code cluster}, {@code startDate}, {@code endDate}, {@code period}, {@code minDropPct})
 * y ejecuta la consulta paginada contra el repositorio.</p>
 *
 * <p>La paginación se maneja mediante {@link Pageable} directamente en la consulta,
 * evitando la carga en memoria de todos los resultados.</p>
 */
@Service
@RequiredArgsConstructor
public class GetConcentrationFilteredQueryUseCase {
    private final ConcentrationRepository repository;

    /**
     * Ejecuta la consulta paginada con los filtros del query.
     *
     * @param query query con los criterios de búsqueda y paginación
     * @return página de métricas de concentración
     */
    @Transactional(readOnly = true)
    public Page<NetworkConcentration> handle(GetConcentrationFilteredQuery query) {
        var spec = buildSpecification(query);
        var pageable = Pageable.ofSize(query.size()).withPage(query.page());
        return repository.findAll(spec, pageable);
    }

    private Specification<NetworkConcentration> buildSpecification(GetConcentrationFilteredQuery query) {
        return (root, query_, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (query.cluster() != null) {
                // Subquery: cluster está en Antenna, no en NetworkConcentration
                var subquery = query_.subquery(String.class);
                var antennaRoot = subquery.from(Antenna.class);
                subquery.select(antennaRoot.get("ecgi").get("value"))
                        .where(cb.equal(antennaRoot.get("cluster").get("value"), query.cluster()));
                predicates.add(root.get("ecgi").get("value").in(subquery));
            }

            if (query.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dayDate"), query.startDate()));
            }
            if (query.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dayDate"), query.endDate()));
            }
            if (query.period() != null) {
                predicates.add(cb.equal(root.get("sessionPeriod"), SessionPeriod.from(query.period())));
            }
            if (query.minDropPct() != null) {
                predicates.add(cb.greaterThan(root.get("concentrationMetrics").get("dropPct"), query.minDropPct()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
