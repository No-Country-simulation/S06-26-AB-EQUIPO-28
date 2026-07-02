package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources.AssistantQueryResourceDocs;

import java.util.List;

import static tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources.AssistantQueryResourceDocs.DESCRIPTION;

@Schema(description = DESCRIPTION)
public record AssistantQueryResource(
        @NotBlank(message = "Question is required")
        @Schema(description = AssistantQueryResourceDocs.QUESTION, example = "¿Cuáles son las regiones más vulnerables?")
        String question,
        @Schema(description = AssistantQueryResourceDocs.CONTEXT)
        List<String> context,
        @Schema(description = AssistantQueryResourceDocs.HISTORY)
        List<ConversationEntryResource> history
) {
    @Schema(description = "Entrada individual del historial de conversación")
    public record ConversationEntryResource(
            @Schema(description = AssistantQueryResourceDocs.HISTORY_ROLE, example = "user")
            String role,
            @Schema(description = AssistantQueryResourceDocs.HISTORY_CONTENT, example = "Háblame sobre inclusión digital")
            String content
    ) { }
}