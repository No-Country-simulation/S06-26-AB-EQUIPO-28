package tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.specifications;

import org.springframework.data.jpa.domain.Specification;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for dynamic {@link MentorshipProgram} queries.
 *
 * <p>These specifications compose with
 * {@link tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository}
 * to enable filtered searches from
 * {@link tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramsQuery}.</p>
 *
 * <p>The embedded Value Objects ({@code MentorshipFocusArea}, {@code MentorshipModality},
 * {@code MentorshipTargetAudience}) are accessed via {@code root.get("voName").get("value")}
 * because they are {@code @Embeddable} records with a {@code String value} field.</p>
 */
public final class MentorshipProgramSpecification {

    private MentorshipProgramSpecification() {
    }

    /**
     * Filters programs by focus area (e.g., TECH, EMPLOYMENT, HEALTH).
     *
     * <p>The focus area is stored as an embedded {@code MentorshipFocusArea}
     * record whose column is mapped as {@code focus_area} and whose field
     * is {@code value}.</p>
     *
     * @param focusArea the focus area value (case-sensitive, must match the normalized stored value)
     * @return a specification filtering by focus area
     */
    public static Specification<MentorshipProgram> hasFocusArea(String focusArea) {
        return (root, query, cb) -> {
            if (focusArea == null || focusArea.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("focusArea").get("value"), focusArea.toUpperCase().trim());
        };
    }

    /**
     * Filters programs by delivery modality (REMOTE, IN_PERSON, HYBRID).
     *
     * @param modality the modality value (case-insensitive)
     * @return a specification filtering by modality
     */
    public static Specification<MentorshipProgram> hasModality(String modality) {
        return (root, query, cb) -> {
            if (modality == null || modality.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("modality").get("value"), modality.toUpperCase().trim());
        };
    }

    /**
     * Filters programs by target audience (YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL).
     *
     * <p>Since {@code target_audience} is nullable in the database,
     * a {@code null} or blank filter value produces a conjunction (no filter).</p>
     *
     * @param targetAudience the target audience value (case-insensitive)
     * @return a specification filtering by target audience
     */
    public static Specification<MentorshipProgram> hasTargetAudience(String targetAudience) {
        return (root, query, cb) -> {
            if (targetAudience == null || targetAudience.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("targetAudience").get("value"), targetAudience.toUpperCase().trim());
        };
    }

    /**
     * Filters programs by cluster name.
     *
     * @param clusterName the cluster name
     * @return a specification filtering by cluster name
     */
    public static Specification<MentorshipProgram> hasClusterName(String clusterName) {
        return (root, query, cb) -> {
            if (clusterName == null || clusterName.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("clusterName"), clusterName);
        };
    }

    /**
     * Filters programs by target income level.
     *
     * @param targetIncomeLevel the income level (e.g., "LOW", "MEDIUM")
     * @return a specification filtering by income level
     */
    public static Specification<MentorshipProgram> hasTargetIncomeLevel(String targetIncomeLevel) {
        return (root, query, cb) -> {
            if (targetIncomeLevel == null || targetIncomeLevel.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("targetIncomeLevel"), targetIncomeLevel);
        };
    }

    /**
     * Filters programs by active status.
     *
     * @param isActive {@code true} for active programs, {@code false} for inactive,
     *                 {@code null} to skip this filter
     * @return a specification filtering by active status
     */
    public static Specification<MentorshipProgram> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    /**
     * Combines multiple optional filters into a single AND specification.
     *
     * <p>Convenience method that composes {@link Specification#allOf(Specification[])}
     * with null-safe conversion for optional filter parameters.</p>
     *
     * @param focusArea optional focus area filter
     * @param modality optional modality filter
     * @param clusterName optional cluster name filter
     * @param targetIncomeLevel optional income level filter
     * @param isActive optional active status filter
     * @return a combined specification
     */
    public static Specification<MentorshipProgram> buildFilter(
            String focusArea,
            String modality,
            String clusterName,
            String targetIncomeLevel,
            Boolean isActive
    ) {
        List<Specification<MentorshipProgram>> specs = new ArrayList<>();
        specs.add(hasFocusArea(focusArea));
        specs.add(hasModality(modality));
        specs.add(hasClusterName(clusterName));
        specs.add(hasTargetIncomeLevel(targetIncomeLevel));
        specs.add(isActive(isActive));

        return specs.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }
}