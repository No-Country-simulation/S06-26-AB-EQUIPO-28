package tech.nocountry.talent.appbitservice.mentorship.application.internal.queryservices.usecases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipGapsQuery;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.MentorshipGapResult;
import tech.nocountry.talent.appbitservice.mentorship.infrastructure.persistence.jpa.repositories.MentorshipProgramRepository;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.InclusionCoreAclPort;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.VulnerableClusterAclResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GetMentorshipGapsUseCase}.
 *
 * <p>Uses plain Mockito (no Spring context) via {@link MockitoExtension}. The
 * dependencies {@link InclusionCoreAclPort} and {@link MentorshipProgramRepository}
 * are mocked directly.</p>
 */
@ExtendWith(MockitoExtension.class)
class GetMentorshipGapsUseCaseTest {

    @Mock
    private MentorshipProgramRepository repo;

    @Mock
    private InclusionCoreAclPort inclusionCoreAclPort;

    @InjectMocks
    private GetMentorshipGapsUseCase useCase;

    @Test
    @DisplayName("execute: priority cluster with no programs returns CRITICAL gap")
    void execute_ClusterWithNoPrograms_ReturnsCriticalGap() {
        var vulnerableCluster = new VulnerableClusterAclResult(
                "clusterA", 85, "HIGH", 5000, 10000, 0.5, "LOW", 0.0, true);

        when(inclusionCoreAclPort.getVulnerableRegions(15, 30, false))
                .thenReturn(List.of(vulnerableCluster));
        when(repo.findByIsActiveTrue()).thenReturn(List.of());

        var query = GetMentorshipGapsQuery.withDefaults();
        List<MentorshipGapResult> gaps = useCase.execute(query);

        assertThat(gaps).hasSize(1);
        assertThat(gaps.get(0).gapSeverity()).isEqualTo(MentorshipGapResult.CRITICAL);
        assertThat(gaps.get(0).clusterName()).isEqualTo("clusterA");
        assertThat(gaps.get(0).hasMentorshipPrograms()).isFalse();
    }
}
