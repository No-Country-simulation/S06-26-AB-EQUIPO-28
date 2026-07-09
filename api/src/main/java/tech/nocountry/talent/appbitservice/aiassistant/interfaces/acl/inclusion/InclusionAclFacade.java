package tech.nocountry.talent.appbitservice.aiassistant.interfaces.acl.inclusion;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices.InclusionDataPort;
import tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices.PromptBuilderService;
import tech.nocountry.talent.appbitservice.aiassistant.domain.exceptions.InclusionDataUnavailableException;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.internal.healthvulnerability.HealthVulnerabilityInternalEndpoint;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.internal.mentalhealth.MentalHealthInternalEndpoint;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.MentalHealthReportResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.VulnerableRegionResource;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.queries.GetMentorshipProgramsQuery;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship.MentorshipGapInternalEndpoint;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.internal.mentorship.MentorshipProgramInternalEndpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ACL Facade for accessing Inclusion Core data.
 * Uses direct injection pattern (Gastro Suite) for in-process communication.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class InclusionAclFacade implements InclusionDataPort {
    private final HealthVulnerabilityInternalEndpoint healthVulnerabilityInternalEndpoint;
    private final MentalHealthInternalEndpoint mentalHealthInternalEndpoint;
    private final MentorshipProgramInternalEndpoint mentorshipProgramInternalEndpoint;
    private final MentorshipGapInternalEndpoint mentorshipGapInternalEndpoint;
    private final ObjectMapper objectMapper;

    @Override
    public PromptBuilderService.InclusionData getDataForQuery(String question) {
        log.debug("Fetching inclusion data for query: {}", question);

        String lowerQuestion = question.toLowerCase();
        String vulnerableRegions = null;
        String mentalHealthReport = null;
        String employabilityData = null;
        String mentorshipData = "No mentorship data available for this query";

        // Determine which data to fetch based on keywords
        if (containsAny(lowerQuestion, "mentor", "mentoria", "program", "tutor", "lider", "comunitari")) {
            mentorshipData = fetchMentorshipData(question);
        }

        if (containsAny(lowerQuestion, "vulnerable", "region", "brecha", "gap", "digital")) {
            vulnerableRegions = fetchVulnerableRegions();
        }

        if (containsAny(lowerQuestion, "mental", "health", "salud", "vulnerability")) {
            mentalHealthReport = fetchMentalHealthReport();
        }

        if (containsAny(lowerQuestion, "employment", "empleo", "employability", "labor")) {
            employabilityData = fetchEmployabilityData();
        }

        // If no specific data identified, fetch all for general context
        if (vulnerableRegions == null && mentalHealthReport == null && employabilityData == null
                && "No mentorship data available for this query".equals(mentorshipData)) {
            log.debug("No specific keywords found, fetching all data");
            vulnerableRegions = fetchVulnerableRegions();
            mentalHealthReport = fetchMentalHealthReport();
            employabilityData = fetchEmployabilityData();
        }

        return new PromptBuilderService.InclusionData(
                vulnerableRegions != null ? vulnerableRegions : "No data available",
                mentalHealthReport != null ? mentalHealthReport : "No data available",
                employabilityData != null ? employabilityData : "No data available",
                mentorshipData
        );
    }

    private String fetchMentorshipData(String question) {
        try {
            log.info("Fetching mentorship data for question: {}", question);
            var programsPage = mentorshipProgramInternalEndpoint.getPrograms(GetMentorshipProgramsQuery.withDefaults());
            var gaps = mentorshipGapInternalEndpoint.getGaps(60, 20, null);
            Map<String, Object> data = new HashMap<>();
            data.put("programs", programsPage.content());
            data.put("gaps", gaps);
            return objectMapper.writeValueAsString(data);
        } catch (JacksonException e) {
            log.warn("Failed to serialize mentorship data, using fallback toString", e);
            try {
                var programsPage = mentorshipProgramInternalEndpoint.getPrograms(GetMentorshipProgramsQuery.withDefaults());
                var gaps = mentorshipGapInternalEndpoint.getGaps(60, 20, null);
                return Map.of("programs", programsPage.content(), "gaps", gaps).toString();
            } catch (Exception fallbackEx) {
                log.error("Fallback serialization also failed", fallbackEx);
                return "No mentorship data available for this query";
            }
        } catch (Exception e) {
            log.error("Error fetching mentorship data", e);
            return "No mentorship data available for this query";
        }
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String fetchVulnerableRegions() {
        try {
            List<VulnerableRegionResource> regions = healthVulnerabilityInternalEndpoint.getVulnerableRegions(
                    0,
                    5,
                    false
            );
            return toJson(regions);
        } catch (Exception e) {
            throw new InclusionDataUnavailableException("Error fetching vulnerable regions: " + e.getMessage(), e);
        }
    }

    private String fetchMentalHealthReport() {
        try {
            MentalHealthReportResource report = mentalHealthInternalEndpoint.getMentalHealthReport(
                    null,
                    false
            );
            return toJson(report);
        } catch (Exception e) {
            throw new InclusionDataUnavailableException("Error fetching mental health report: " + e.getMessage(), e);
        }
    }

    private String fetchEmployabilityData() {
        try {
            List<VulnerableRegionResource> regions = healthVulnerabilityInternalEndpoint.getVulnerableRegions(
                    0,
                    5,
                    false
            );
            return "[PROXY DATA — uses vulnerable regions as proxy for employment gaps] " + toJson(regions);
        } catch (Exception e) {
            throw new InclusionDataUnavailableException("Error fetching employability data: " + e.getMessage(), e);
        }
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            log.debug("Error serializing object, falling back to toString: {}", e.getMessage());
            return object.toString();
        }
    }
}