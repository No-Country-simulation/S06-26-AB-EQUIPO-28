package tech.nocountry.talent.appbitservice.aiassistant.infrastructure.llm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration properties for OpenRouter LLM integration.
 * Maps to spring.ai.openai.* properties — Spring AI standard namespace.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
@Getter
@Setter
public class OpenRouterProperties {
    private String apiKey;
    private String baseUrl;
    private Duration timeout = Duration.ofSeconds(120);
    private Chat chat = new Chat();

    /**
     * Convenience accessor for the configured chat model
     * (spring.ai.openai.chat.options.model).
     *
     * @return the model identifier, or null if not configured
     */
    public String getModel() {
        return chat.getOptions().getModel();
    }

    @Getter
    @Setter
    public static class Chat {
        private Options options = new Options();
    }

    @Getter
    @Setter
    public static class Options {
        private String model;
    }
}
