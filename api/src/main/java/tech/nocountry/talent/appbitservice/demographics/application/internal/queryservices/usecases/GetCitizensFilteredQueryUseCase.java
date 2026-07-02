package tech.nocountry.talent.appbitservice.demographics.application.internal.queryservices.usecases;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates.CitizenProfile;
import tech.nocountry.talent.appbitservice.demographics.domain.model.queries.GetCitizensFilteredQuery;
import tech.nocountry.talent.appbitservice.demographics.infrastructure.persistence.jpa.repositories.CitizenProfileRepository;

import java.util.ArrayList;

/**
 * Caso de Uso Atómico: Obtener ciudadanos filtrados por nivel de ingreso y/o grupo de edad.
 *
 * <p>Recupera una página de {@link CitizenProfile} aplicando filtros opcionales
 * por {@code incomeLevel} y {@code ageGroup} mediante {@link Specification} dinámica.
 * Siempre retorna resultados paginados.</p>
 */
@Service
@RequiredArgsConstructor
public class GetCitizensFilteredQueryUseCase {

    private final CitizenProfileRepository repository;

    /**
     * Ejecuta la consulta con los filtros y paginación especificados.
     *
     * @param query parámetros de filtrado y paginación
     * @return página de perfiles de ciudadanos que coinciden con los filtros
     */
    @Transactional(readOnly = true)
    public Page<CitizenProfile> handle(GetCitizensFilteredQuery query) {
        var spec = buildSpecification(query);
        var pageable = org.springframework.data.domain.Pageable.ofSize(query.size())
                .withPage(query.page());
        return repository.findAll(spec, pageable);
    }

    /**
     * Construye una {@link Specification} dinámica basada en los filtros presentes en la query.
     *
     * @param query la consulta con filtros opcionales
     * @return especificación JPA para la búsqueda
     */
    private Specification<CitizenProfile> buildSpecification(GetCitizensFilteredQuery query) {
        return (root, _, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (query.incomeLevel() != null) {
                predicates.add(cb.equal(root.get("incomeLevel").get("value"), query.incomeLevel()));
            }
            if (query.ageGroup() != null) {
                predicates.add(cb.equal(root.get("ageGroup").get("value"), query.ageGroup()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
