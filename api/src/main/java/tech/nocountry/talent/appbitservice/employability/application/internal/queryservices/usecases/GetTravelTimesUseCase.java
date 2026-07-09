package tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.TravelTime;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetTravelTimesQuery;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.TravelTimeRepository;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications.TravelTimeSpecification;

/**
 * Atomic query use case for paginated and filtered inter-cluster travel-time searches.
 *
 * <p>Compone filtros Spring Data {@link org.springframework.data.jpa.domain.Specification}
 * vía {@link TravelTimeSpecification#buildFilter} y aplica paginación con parseo
 * dinámico de sort. Sigue el mismo patrón que {@code GetMobilityODPairsUseCase}.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetTravelTimesUseCase {

    private final TravelTimeRepository repository;

    /**
     * Ejecuta la query paginada de tiempos de viaje con filtros opcionales.
     *
     * @param query la query con paginación, sort y filtros
     * @return una página de {@link TravelTime} que coinciden con los criterios
     */
    public Page<TravelTime> execute(GetTravelTimesQuery query) {
        var specification = TravelTimeSpecification.buildFilter(
                query.originCluster(),
                query.destinationCluster()
        );
        var pageable = PageRequest.of(query.page(), query.size(), parseSort(query.sort()));
        return repository.findAll(specification, pageable);
    }

    /**
     * Parsea un string de sort en formato {@code "field,direction"} a un
     * {@link Sort} de Spring Data.
     *
     * @param sort el string de sort (ej: "createdAt,desc")
     * @return un {@link Sort}, o {@code Sort.unsorted()} si falla el parseo
     */
    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }
        var parts = sort.split(",");
        if (parts.length != 2) {
            return Sort.unsorted();
        }
        var direction = Sort.Direction.fromOptionalString(parts[1].trim())
                .orElse(Sort.Direction.DESC);
        return Sort.by(direction, parts[0].trim());
    }
}
