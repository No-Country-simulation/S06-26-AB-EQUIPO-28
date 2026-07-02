package tech.nocountry.talent.appbitservice.inclusioncore.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions.RegionNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Use case for health vulnerability data.
 *
 * <p>Legacy — the health_vulnerability_index table has been removed.
 * This class is kept for compilation but returns empty/not-found.
 * The main endpoint uses GetVulnerableRegionsQueryUseCase (on-the-fly).</p>
 */
@Service
@RequiredArgsConstructor
public class GetAllHealthVulnerabilitiesQueryUseCase {

    public List<?> execute() {
        return List.of();
    }

    public Object executeOrThrow(String regionName) {
        throw new RegionNotFoundException(regionName);
    }

    public Optional<?> executeOptional(String regionName) {
        return Optional.empty();
    }

    public long countByMinVulnerabilityScore(int minVulnerabilityScore) {
        return 0;
    }
}
