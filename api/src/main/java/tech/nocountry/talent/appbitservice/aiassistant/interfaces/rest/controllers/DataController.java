package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.internal.ProcessAssistantQueryEndpoint;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.DataDocs;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantQueryResource;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantResponseResource;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller that wraps the AI assistant in the challenge's expected format.
 *
 * <p>POST /api/v1/data/query — Accepts { query, filters: { region, indicator }, language }
 * and returns { aiResponse, data: [{ region, value, source }], sources }.</p>
 *
 * <p>This controller adapts the existing AI assistant pipeline to the challenge's
 * API contract without duplicating business logic.</p>
 */
@RestController
@RequestMapping(value = "/api/v1/data", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class DataController implements DataDocs {

    private final ProcessAssistantQueryEndpoint endpoint;

    /**
     * POST /api/v1/data/query — Process a natural-language query in challenge format.
     *
     * @param request the challenge-format request body
     * @return challenge-format response with AI answer, structured data, and sources
     */
    @Override
    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> processQuery(
            @Valid @RequestBody DataRequest request) {

        // Map challenge format → existing assistant format
        AssistantQueryResource queryResource = mapToAssistantQuery(request);

        // Process through existing AI pipeline
        AssistantResponseResource assistantResponse = endpoint.processQuery(queryResource);

        // Map assistant response → challenge format
        Map<String, Object> response = mapToChallengeResponse(assistantResponse, request);

        return ResponseEntity.ok(response);
    }

    private AssistantQueryResource mapToAssistantQuery(DataRequest request) {
        // Build context from filters
        java.util.List<String> context = new java.util.ArrayList<>();
        if (request.filters() != null) {
            if (request.filters().region() != null) {
                context.add("region: " + request.filters().region());
            }
            if (request.filters().indicator() != null) {
                context.add("indicator: " + request.filters().indicator());
            }
        }
        if (request.language() != null) {
            context.add("language: " + request.language());
        }

        return new AssistantQueryResource(
                request.query(),
                context,
                null // no conversation history in challenge format
        );
    }

    private Map<String, Object> mapToChallengeResponse(
            AssistantResponseResource assistantResponse, DataRequest request) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("aiResponse", assistantResponse.answer());

        // Build structured data array from the AI response metadata
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        if (assistantResponse.sources() != null) {
            for (String source : assistantResponse.sources()) {
                Map<String, Object> datum = new LinkedHashMap<>();
                datum.put("region", request.filters() != null && request.filters().region() != null
                        ? request.filters().region() : "all");
                datum.put("value", source);
                datum.put("source", "Visent CDRView");
                data.add(datum);
            }
        }
        response.put("data", data);
        response.put("sources", assistantResponse.sources() != null
                ? assistantResponse.sources() : List.of("Visent CDRView"));
        response.put("timestamp", Instant.now().toString());

        return response;
    }

    /**
     * Request DTO for the /api/v1/data/query endpoint.
     */
    public record DataRequest(
            String query,
            Filters filters,
            String language
    ) {
        public record Filters(
                String region,
                String indicator
        ) { }
    }
}
