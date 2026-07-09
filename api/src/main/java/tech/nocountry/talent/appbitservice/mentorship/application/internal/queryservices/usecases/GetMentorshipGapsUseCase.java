package tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipGapsQuery;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.MentorshipGapResult;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.InclusionCoreAclPort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Atomic query use case for mentorship gap analysis.
 *
 * <p>Crosses vulnerability data from inclusion-core (via ACL) with the mentorship
 * program catalog to identify clusters with high vulnerability but insufficient
 * program coverage. This is the most complex use case in the mentorship BC.</p>
 *
 * <p>Gap severity taxonomy:</p>
 * <ul>
 *   <li>{@code CRITICAL} — priority intervention cluster with no programs</li>
 *   <li>{@code HIGH} — vulnerability above threshold with no programs</li>
 *   <li>{@code MODERATE} — programs exist or vulnerability below threshold</li>
 * </ul>
 *
 * <strong>Complex Use Case — 4 Step Pipeline</strong>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMentorshipGapsUseCase {

    private final InclusionCoreAclPort inclusionCoreAclPort;
    private final MentorshipProgramRepository repository;

    /**
     * Executes the gap analysis across vulnerability data and program catalog.
     *
     * @param query gap analysis parameters
     * @return list of {@link MentorshipGapResult} sorted by vulnerability desc, limited to maxResults
     */
    public List<MentorshipGapResult> execute(GetMentorshipGapsQuery query) {
        // Step 1: Get vulnerable regions from inclusion-core via ACL
        var vulnerable = inclusionCoreAclPort.getVulnerableRegions(
                query.minVulnerabilityScore(), query.maxResults(), false);

        // Step 2: Get all active programs from mentorship catalog
        var activePrograms = repository.findByIsActiveTrue();

        // Step 3: Group programs by cluster name for fast lookup
        Map<String, List<MentorshipProgram>> programsByCluster = activePrograms.stream()
                .collect(Collectors.groupingBy(MentorshipProgram::getClusterName));

        // Step 4: For each vulnerable cluster, compute gap
        var gaps = new ArrayList<MentorshipGapResult>();
        for (var cv : vulnerable) {
            List<MentorshipProgram> matching = programsByCluster
                    .getOrDefault(cv.clusterName(), List.of());

            // Apply focus area filter if query specifies one
            if (query.focusArea() != null && !query.focusArea().isBlank()) {
                String targetArea = query.focusArea().toUpperCase().trim();
                matching = matching.stream()
                        .filter(p -> p.getFocusArea().getValue().equals(targetArea))
                        .toList();
            }

            boolean hasPrograms = !matching.isEmpty();
            String gapSeverity = computeGapSeverity(cv.isPriorityForIntervention(),
                    cv.vulnerabilityScore(), query.minVulnerabilityScore(), hasPrograms);

            var result = new MentorshipGapResult(
                    cv.clusterName(),
                    cv.vulnerabilityScore(),
                    cv.vulnerablePopulationCount(),
                    cv.connectivityLevel(),
                    hasPrograms,
                    matching.stream().map(MentorshipProgram::getProgramId).toList(),
                    gapSeverity
            );
            gaps.add(result);
        }

        // Sort by vulnerability score descending, limit to maxResults
        return gaps.stream()
                .sorted(Comparator.comparingInt(MentorshipGapResult::vulnerabilityScore).reversed())
                .limit(query.maxResults())
                .toList();
    }

    /**
     * Computes the gap severity based on vulnerability and program presence.
     */
    private String computeGapSeverity(boolean isPriority, int vulnScore, int minScore, boolean hasPrograms) {
        if (isPriority && !hasPrograms) {
            return MentorshipGapResult.CRITICAL;
        }
        if (vulnScore >= minScore && !hasPrograms) {
            return MentorshipGapResult.HIGH;
        }
        return MentorshipGapResult.MODERATE;
    }
}