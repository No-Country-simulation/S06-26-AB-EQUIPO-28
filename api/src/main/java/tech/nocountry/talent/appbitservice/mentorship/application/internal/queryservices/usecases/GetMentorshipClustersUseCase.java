package tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipClustersQuery;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.MentorshipClusterSummary;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Atomic query use case to enumerate mentorship coverage by geographic cluster.
 *
 * <p>Groups all programs by {@code clusterName}, computes aggregate statistics
 * (counts, capacities, distinct focus areas/modalities/audiences), and returns
 * a list of {@link MentorshipClusterSummary} sorted alphabetically by cluster name.</p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMentorshipClustersUseCase {

    private final MentorshipProgramRepository repository;

    /**
     * Executes the cluster summary query.
     *
     * @param query the query with optional focusArea filter
     * @return list of {@link MentorshipClusterSummary} sorted by cluster name
     */
    public List<MentorshipClusterSummary> execute(GetMentorshipClustersQuery query) {
        // Step 1: Load all programs
        List<MentorshipProgram> all = repository.findAll();

        // Step 2: Filter by focusArea if present
        if (query.focusArea() != null && !query.focusArea().isBlank()) {
            String target = query.focusArea().toUpperCase().trim();
            all = all.stream()
                    .filter(p -> p.getFocusArea().getValue().equals(target))
                    .toList();
        }

        // Step 3: Group by cluster name
        Map<String, List<MentorshipProgram>> programsByCluster = all.stream()
                .collect(Collectors.groupingBy(MentorshipProgram::getClusterName));

        // Step 4: Build summaries
        var summaries = new ArrayList<MentorshipClusterSummary>();
        for (var entry : programsByCluster.entrySet()) {
            var cluster = entry.getKey();
            var programs = entry.getValue();
            var active = programs.stream().filter(MentorshipProgram::isActive).toList();

            Set<String> focusAreas = programs.stream()
                    .map(p -> p.getFocusArea().getValue())
                    .collect(Collectors.toSet());
            Set<String> modalities = programs.stream()
                    .map(p -> p.getModality().getValue())
                    .collect(Collectors.toSet());
            Set<String> targetAudiences = programs.stream()
                    .filter(p -> p.getTargetAudience() != null && p.getTargetAudience().getValue() != null)
                    .map(p -> p.getTargetAudience().getValue())
                    .collect(Collectors.toSet());

            int totalCapacity = programs.stream()
                    .mapToInt(p -> p.getTotalCapacity() != null ? p.getTotalCapacity() : 0)
                    .sum();
            int totalActiveMentees = programs.stream()
                    .mapToInt(MentorshipProgram::getActiveMentees)
                    .sum();

            var summary = new MentorshipClusterSummary(
                    cluster,
                    programs.size(),
                    active.size(),
                    focusAreas,
                    modalities,
                    targetAudiences,
                    totalCapacity,
                    totalActiveMentees,
                    programs.stream().map(MentorshipProgram::getProgramId).toList()
            );
            summaries.add(summary);
        }

        // Sort by cluster name alphabetically
        summaries.sort(Comparator.comparing(MentorshipClusterSummary::clusterName));
        return summaries;
    }
}