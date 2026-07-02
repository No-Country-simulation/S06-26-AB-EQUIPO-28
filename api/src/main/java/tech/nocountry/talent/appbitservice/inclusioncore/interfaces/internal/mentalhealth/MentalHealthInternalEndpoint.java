package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.internal.mentalhealth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.inclusioncore.application.internal.queryservices.usecases.GetMentalHealthReportQueryUseCase;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.queries.GetMentalHealthReportQuery;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.MentalHealthReportResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.transform.MentalHealthReportResourceAssembler;

/**
 * Internal endpoint for mental health report data.
 *
 * <p>These endpoints are consumed by other BCs (e.g., ai-assistant) and by REST controllers
 * to access mental health report data without going through the public API.
 * Implements the ACL pattern for in-process communication.</p>
 */
@Component
@RequiredArgsConstructor
public class MentalHealthInternalEndpoint {
    private final GetMentalHealthReportQueryUseCase getMentalHealthReportQueryUseCase;
    private final MentalHealthReportResourceAssembler resourceAssembler;

    /**
     * Gets the mental health report for a specific period.
     *
     * @param reportPeriod the report period (e.g., "2024-Q1")
     * @param includePriorityOnly whether to include only priority regions
     * @return the mental health report resource
     */
    public MentalHealthReportResource getMentalHealthReport(
            String reportPeriod,
            boolean includePriorityOnly
    ) {
        var effectivePeriod = (reportPeriod != null && !reportPeriod.isBlank())
                ? reportPeriod
                : GetMentalHealthReportQuery.currentPeriod().reportPeriod();
        var query = new GetMentalHealthReportQuery(effectivePeriod, includePriorityOnly, 0);
        var report = getMentalHealthReportQueryUseCase.execute(query);
        return resourceAssembler.toMentalHealthReportResource(report);
    }
}
