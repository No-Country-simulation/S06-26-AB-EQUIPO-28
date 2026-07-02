package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources.AssistantResponseResourceDocs;

import java.time.Instant;
import java.util.List;

import static tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources.AssistantResponseResourceDocs.DESCRIPTION;

@Schema(description = DESCRIPTION)
public record AssistantResponseResource(
        @Schema(description = AssistantResponseResourceDocs.ANSWER)
        String answer,
        @Schema(description = AssistantResponseResourceDocs.SOURCES)
        List<String> sources,
        @Schema(description = AssistantResponseResourceDocs.METADATA)
        ResponseMetadata metadata,
        @Schema(description = AssistantResponseResourceDocs.TIMESTAMP)
        Instant timestamp
) {
    @Schema(description = "Metadatos de la respuesta generada por el LLM")
    public record ResponseMetadata(
            @Schema(description = AssistantResponseResourceDocs.METADATA_MODEL, example = "gpt-4")
            String model,
            @Schema(description = AssistantResponseResourceDocs.METADATA_TOKENS, example = "150")
            int totalTokens
    ) { }
}