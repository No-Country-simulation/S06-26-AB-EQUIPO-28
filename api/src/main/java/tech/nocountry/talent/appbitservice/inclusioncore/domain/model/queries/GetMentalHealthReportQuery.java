package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.queries;

/**
 * Query to retrieve a mental health vulnerability report.
 *
 * <p>This query retrieves a comprehensive report of mental health vulnerability
 * for government decision-makers, including priority regions for intervention.</p>
 *
 * @param reportPeriod the period to retrieve (e.g., "2024-Q1")
 * @param includePriorityOnly if true, only return priority regions
 * @param minVulnerabilityScore minimum vulnerability score filter (default 0)
 */
public record GetMentalHealthReportQuery(
        String reportPeriod,
        boolean includePriorityOnly,
        int minVulnerabilityScore
) {
    /**
     * Creates a query to get the full report for a period.
     *
     * @param reportPeriod the period for the report
     * @return new GetMentalHealthReportQuery
     */
    public static GetMentalHealthReportQuery fullReport(String reportPeriod) {
        return new GetMentalHealthReportQuery(reportPeriod, false, 0);
    }

    /**
     * Creates a query to get only priority regions.
     *
     * @param reportPeriod the period for the report
     * @return new GetMentalHealthReportQuery
     */
    public static GetMentalHealthReportQuery priorityOnly(String reportPeriod) {
        return new GetMentalHealthReportQuery(reportPeriod, true, 0);
    }

    /**
     * Creates a query for the current period.
     *
     * @return new GetMentalHealthReportQuery for current period
     */
    public static GetMentalHealthReportQuery currentPeriod() {
        String currentPeriod = java.time.Year.now() + "-Q" + 
                ((java.time.Month.from(java.time.LocalDate.now()).getValue() - 1) / 3 + 1);
        return new GetMentalHealthReportQuery(currentPeriod, false, 0);
    }
}