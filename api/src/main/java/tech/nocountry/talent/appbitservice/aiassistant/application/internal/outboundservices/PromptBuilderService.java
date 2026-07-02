package tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import tech.nocountry.talent.appbitservice.aiassistant.domain.model.commands.AssistantQueryCommand;

import java.util.ArrayList;
import java.util.List;

import static tech.nocountry.talent.appbitservice.aiassistant.infrastructure.llm.PromptTemplates.ANALYSIS_FOOTER;
import static tech.nocountry.talent.appbitservice.aiassistant.infrastructure.llm.PromptTemplates.SYSTEM_PROMPT_TEMPLATE;

/**
 * Service for building structured prompts from domain data.
 * Constructs system prompts, conversation history, and user questions
 * using inclusion data, conversation history, and the user's query.
 */
@Service
public class PromptBuilderService {

    /**
     * Builds the system prompt from inclusion data.
     *
     * @param inclusionData the inclusion core data to include in the prompt
     * @return the system prompt string
     */
    public String buildSystemPrompt(InclusionData inclusionData) {
        String result = SYSTEM_PROMPT_TEMPLATE;
        result = result.replace("{{VULNERABLE_REGIONS}}", inclusionData.vulnerableRegions());
        result = result.replace("{{MENTAL_HEALTH_REPORT}}", inclusionData.mentalHealthReport());
        result = result.replace("{{EMPLOYABILITY_DATA}}", inclusionData.employabilityData());
        return result;
    }

    /**
     * Builds the conversation history as a list of Spring AI messages.
     *
     * @param command the query command containing the history
     * @return list of Message objects (UserMessage, AssistantMessage)
     */
    public List<Message> buildHistory(AssistantQueryCommand command) {
        if (command.history() == null || command.history().isEmpty()) {
            return List.of();
        }

        List<Message> messages = new ArrayList<>();
        for (AssistantQueryCommand.ConversationEntry entry : command.history()) {
            Message message = switch (entry.role().toLowerCase()) {
                case "user" -> new UserMessage(entry.content());
                case "assistant" -> new AssistantMessage(entry.content());
                default -> throw new IllegalArgumentException("Unknown role: " + entry.role());
            };
            messages.add(message);
        }
        return messages;
    }

    /**
     * Builds the user question string from the query command.
     *
     * @param command the query command from the manager
     * @return the user question string
     */
    public String buildUserQuestion(AssistantQueryCommand command) {
        StringBuilder question = new StringBuilder();

        // Add context parameters if present
        if (command.context() != null && !command.context().isEmpty()) {
            question.append("Context: ");
            question.append(String.join(", ", command.context()));
            question.append("\n\n");
        }

        question.append(command.question());
        question.append(ANALYSIS_FOOTER);

        return question.toString();
    }

    /**
     * Data container for inclusion core information.
     *
     * @param vulnerableRegions  regions with vulnerability data
     * @param mentalHealthReport mental health indicators
     * @param employabilityData  employment indicators
     */
    public record InclusionData(
            String vulnerableRegions,
            String mentalHealthReport,
            String employabilityData
    ) {
    }
}
