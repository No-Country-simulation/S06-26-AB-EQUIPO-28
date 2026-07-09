package tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramsQuery;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.specifications.MentorshipProgramSpecification;

/**
 * Atomic query use case for paginated and filtered mentorship program searches.
 *
 * <p>Composes Spring Data {@link org.springframework.data.jpa.domain.Specification}
 * filters via {@link MentorshipProgramSpecification#buildFilter} and applies
 * pagination with dynamic sort parsing.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMentorshipProgramsUseCase {

    private final MentorshipProgramRepository repository;

    /**
     * Executes the paginated programs query with optional filters.
     *
     * @param query the query with pagination, sort, and filter parameters
     * @return a page of {@link MentorshipProgram} matching the criteria
     */
    public Page<MentorshipProgram> execute(GetMentorshipProgramsQuery query) {
        var specification = MentorshipProgramSpecification.buildFilter(
                query.focusArea(),
                query.modality(),
                query.clusterName(),
                query.targetIncomeLevel(),
                query.isActive()
        );
        var pageable = PageRequest.of(query.page(), query.size(), parseSort(query.sort()));
        return repository.findAll(specification, pageable);
    }

    /**
     * Parses a sort string in format {@code "field,direction"} into a Spring Data
     * {@link Sort} object.
     *
     * <p>Example: {@code "createdAt,desc"} → {@code Sort.by(Sort.Direction.DESC, "createdAt")}.
     * Invalid sort fields are silently returned as unsorted (they become a no-op Sort).</p>
     *
     * @param sort the sort string (e.g., "createdAt,desc")
     * @return a {@link Sort} object, or {@code Sort.unsorted()} on parse failure
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