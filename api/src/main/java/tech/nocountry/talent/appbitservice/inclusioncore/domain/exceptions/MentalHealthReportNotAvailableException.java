package tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions;

/**
 * Exception thrown when mental health report data is not available.
 */
public class MentalHealthReportNotAvailableException extends InclusionCoreDomainException {

    public MentalHealthReportNotAvailableException(String reportPeriod) {
        super(String.format("Mental health report not available for period: %s", reportPeriod));
    }

    public MentalHealthReportNotAvailableException(String reportPeriod, Throwable cause) {
        super(String.format("Mental health report not available for period: %s", reportPeriod), cause);
    }
}