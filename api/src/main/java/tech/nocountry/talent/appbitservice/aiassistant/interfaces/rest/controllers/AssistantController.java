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
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.AssistantDocs;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantQueryResource;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantResponseResource;

/**
 * REST controller for the AI Assistant API.
 * Handles queries from government managers in natural language.
 */
@RestController
@RequestMapping(value = "/api/v1/assistant", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class AssistantController implements AssistantDocs {
    private final ProcessAssistantQueryEndpoint endpoint;

    @Override
    @PostMapping("/queries")
    public ResponseEntity<AssistantResponseResource> processQuery(@Valid @RequestBody AssistantQueryResource resource) {
        var response = endpoint.processQuery(resource);
        return ResponseEntity.ok(response);
    }
}