package tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.employability.domain.model.aggregates.MobilityODPair;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetMobilityODPairsQuery;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.repositories.MobilityODPairRepository;
import tech.nocountry.talent.appbitservice.employability.infrastructure.persistence.jpa.specifications.MobilityODPairSpecification;

/**
 * Atomic query use case for paginated and filtered mobility OD pair searches.
 *
 * <p>Compone filtros Spring Data {@link org.springframework.data.jpa.domain.Specification}
 * vía {@link MobilityODPairSpecification#buildFilter} y aplica paginación con parseo
 * dinámico de sort. Sigue el mismo patrón que {@code GetMentorshipProgramsUseCase}.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMobilityODPairsUseCase {

    private final MobilityODPairRepository repository;

    /**
     * Ejecuta la query paginada de pares OD con filtros opcionales.
     *
     * @param query la query con paginación, sort y filtros
     * @return una página de {@link MobilityODPair} que coinciden con los criterios
     */
    public Page<MobilityODPair> execute(GetMobilityODPairsQuery query) {
        var specification = MobilityODPairSpecification.buildFilter(
                query.originCluster(),
                query.destinationCluster(),
                query.predominantPeriod()
        );
        var pageable = PageRequest.of(query.page(), query.size(), parseSort(query.sort()));
        return repository.findAll(specification, pageable);
    }

    /**
     * Parsea un string de sort en formato {@code "field,direction"} a un
     * {@link Sort} de Spring Data.
     *
     * <p>Ejemplo: {@code "createdAt,desc"} → {@code Sort.by(Sort.Direction.DESC, "createdAt")}.
     * Campos inválidos devuelven unsort (no-op).</p>
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
