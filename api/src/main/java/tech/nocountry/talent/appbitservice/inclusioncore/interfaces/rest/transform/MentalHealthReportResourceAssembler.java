package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.inclusioncore.domain.model.aggregates.MentalHealthReport;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.MentalHealthReportResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.RegionVulnerabilitySummaryResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.ReportMetadataResource;

/**
 * Assembler for transforming MentalHealthReport domain aggregates to REST resources.
 *
 * <p>Provides mapping methods for converting between domain objects and
 * API response DTOs for mental health reports.</p>
 */
@Component
public class MentalHealthReportResourceAssembler {

    /**
     * Transforms a MentalHealthReport to a MentalHealthReportResource.
     *
     * @param report the mental health report
     * @return the resource representation
     */
    public MentalHealthReportResource toMentalHealthReportResource(MentalHealthReport report) {
        var summaries = report.getRegionSummaries().stream()
                .map(summary -> new RegionVulnerabilitySummaryResource(
                        summary.regionName(),
                        summary.vulnerabilityScore().getValue(),
                        summary.vulnerablePercentage(),
                        summary.connectivity().getValue(),
                        summary.isPriority()
                ))
                .toList();

        var metadata = new ReportMetadataResource(
                report.getMetadata().totalVulnerablePopulation(),
                report.getMetadata().totalPopulation(),
                report.getMetadata().averageVulnerabilityScore(),
                report.getMetadata().priorityRegionCount()
        );

        return new MentalHealthReportResource(
                report.getReportId(),
                report.getGeneratedAt(),
                report.getReportPeriod(),
                summaries,
                metadata
        );
    }
}
