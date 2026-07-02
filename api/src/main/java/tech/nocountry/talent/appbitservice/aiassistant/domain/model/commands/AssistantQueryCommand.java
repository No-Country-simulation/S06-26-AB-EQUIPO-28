package tech.nocountry.talent.appbitservice.aiassistant.domain.model.commands;

import java.util.List;
import java.util.Objects;

/**
 * Command representing a query from a government manager to the AI assistant.
 * This is the input model for the assistant query use case.
 *
 * @param question   The natural language question from the manager
 * @param context    Optional context parameters (e.g., region, period)
 * @param history    Optional conversation history for context
 */
public record AssistantQueryCommand(
        String question,
        List<String> context,
        List<ConversationEntry> history
) {
    /**
     * Constructor with validation.
     *
     * @param question the main question
     * @param context  optional context parameters
     * @param history  conversation history
     */
    public AssistantQueryCommand {
        Objects.requireNonNull(question, "question is required");
        if (question.isBlank()) {
            throw new IllegalArgumentException("question cannot be blank");
        }
    }

    /**
     * Single conversation entry in history.
     *
     * @param role    "user" or "assistant"
     * @param content the message content
     */
    public record ConversationEntry(String role, String content) {
        public ConversationEntry {
            Objects.requireNonNull(role, "role is required");
            Objects.requireNonNull(content, "content is required");
        }
    }
}