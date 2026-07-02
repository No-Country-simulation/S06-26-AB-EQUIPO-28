package tech.nocountry.talent.appbitservice.aiassistant.infrastructure.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.aiassistant.domain.exceptions.LlmCommunicationException;

import java.util.List;

/**
 * Implementation of the LLM client using Spring AI with OpenRouter.
 * Supports structured messages (system, history, user) and token counting.
 */
@Component
@Slf4j
public class OpenRouterChatClient {
    private final ChatClient chatClient;
    private final String defaultModel;

    /**
     * Constructor with ChatModel injection.
     *
     * @param chatModel the configured ChatModel (auto-configured by Spring AI)
     */
    public OpenRouterChatClient(ChatModel chatModel, OpenRouterProperties properties) {
        this.chatClient = ChatClient.builder(chatModel).build();
        this.defaultModel = properties.getModel();
        log.info("Initialized OpenRouterChatClient with model: {}", defaultModel);
    }

    /**
     * Sends a chat request to the LLM using structured messages.
     * Includes retry logic with exponential backoff for transient errors (502, 429, 503).
     *
     * @param systemPrompt the system prompt
     * @param history      the conversation history (list of UserMessage/AssistantMessage)
     * @param userQuestion the current user question
     * @return the response from the LLM
     */
    public LlmResponse chat(String systemPrompt, List<Message> history, String userQuestion) {
        int maxRetries = 3;
        long baseDelayMs = 1000;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return doChat(systemPrompt, history, userQuestion);
            } catch (LlmCommunicationException e) {
                throw e; // non-retryable
            } catch (Exception e) {
                String msg = e.getMessage() != null ? e.getMessage() : "";
                boolean isRetryable = msg.contains("502") || msg.contains("429") || msg.contains("503")
                        || msg.contains("Bad Gateway") || msg.contains("Too Many Requests") || msg.contains("Service Unavailable");

                if (!isRetryable || attempt == maxRetries) {
                    throw new LlmCommunicationException("LLM call failed after " + (attempt + 1) + " attempts: " + msg, e);
                }

                long delay = baseDelayMs * (1L << attempt); // 1s, 2s, 4s
                log.warn("LLM call failed (attempt {}/{}), retrying in {}ms: {}", attempt + 1, maxRetries + 1, delay, msg);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new LlmCommunicationException("Interrupted during retry backoff", ie);
                }
            }
        }
        throw new LlmCommunicationException("Unexpected: retry loop exited without result");
    }

    private LlmResponse doChat(String systemPrompt, List<Message> history, String userQuestion) {
        log.debug("Sending chat request to LLM");

        var promptBuilder = chatClient.prompt()
                .system(systemPrompt);

        if (history != null && !history.isEmpty()) {
            promptBuilder.messages(history.toArray(new Message[0]));
        }

        ChatResponse response = promptBuilder
                .user(userQuestion)
                .call()
                .chatResponse();

        if (response == null || response.getResult() == null) {
            throw new LlmCommunicationException("Empty response from LLM");
        }

        Generation generation = response.getResult();
        AssistantMessage assistantMessage = generation.getOutput();
        String content = assistantMessage != null
                ? assistantMessage.getText()
                : "";

        int tokens = 0;
        if (response.getMetadata() != null && response.getMetadata().getUsage() != null) {
            tokens = response.getMetadata().getUsage().getTotalTokens();
        }

        log.debug("Received LLM response with {} tokens", tokens);
        return new LlmResponse(content, defaultModel, tokens);
    }

    /**
     * Response from the LLM.
     *
     * @param content     the response text
     * @param model       the model used
     * @param totalTokens tokens consumed
     */
    public record LlmResponse(String content, String model, int totalTokens) {
    }
}