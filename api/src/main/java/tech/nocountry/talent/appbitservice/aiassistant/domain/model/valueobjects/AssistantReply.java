package tech.nocountry.talent.appbitservice.aiassistant.domain.model.valueobjects;

import java.time.Instant;
import java.util.List;

/**
 * Domain model representing the AI assistant's response.
 * This is the internal representation used within the domain layer.
 *
 * @param answer     The natural language answer from the LLM
 * @param sources    List of data sources used to generate the answer
 * @param metadata   Additional metadata (model used, tokens, etc.)
 * @param timestamp  When the response was generated
 */
public record AssistantReply(
        String answer,
        List<String> sources,
        ResponseMetadata metadata,
        Instant timestamp
) {
    /**
     * Creates a response with required fields.
     *
     * @param answer   the answer text
     * @param sources  the data sources
     * @param model    model used
     * @param tokens   total tokens used
     * @return new response resource
     */
    public static AssistantReply create(String answer, List<String> sources, String model, int tokens) {
        return new AssistantReply(
                answer,
                sources != null ? sources : List.of(),
                new ResponseMetadata(model, tokens),
                Instant.now()
        );
    }

    /**
     * Metadata about the LLM response.
     *
     * @param model          The model used
     * @param totalTokens    Total tokens consumed
     */
    public record ResponseMetadata(String model, int totalTokens) {
    }
}
