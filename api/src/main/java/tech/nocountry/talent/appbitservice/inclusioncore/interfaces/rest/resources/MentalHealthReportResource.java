package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs.resources.MentalHealthReportResourceDocs;

import java.time.Instant;
import java.util.List;

/**
 * REST resource for the mental health report.
 *
 * <p>This DTO represents the mental health vulnerability report
 * for government managers.</p>
 *
 * @param reportId unique report identifier
 * @param generatedAt report generation date
 * @param reportPeriod report period (e.g., "2024-Q1")
 * @param regionSummaries list of region summaries
 * @param metadata report metadata
 */
@Schema(description = MentalHealthReportResourceDocs.DESCRIPTION)
public record MentalHealthReportResource(
        @Schema(description = MentalHealthReportResourceDocs.REPORT_ID)
        String reportId,

        @Schema(description = MentalHealthReportResourceDocs.GENERATED_AT)
        Instant generatedAt,

        @Schema(description = MentalHealthReportResourceDocs.REPORT_PERIOD)
        String reportPeriod,

        @Schema(description = MentalHealthReportResourceDocs.REGION_SUMMARIES)
        List<RegionVulnerabilitySummaryResource> regionSummaries,

        @Schema(description = MentalHealthReportResourceDocs.METADATA)
        ReportMetadataResource metadata
) {}
