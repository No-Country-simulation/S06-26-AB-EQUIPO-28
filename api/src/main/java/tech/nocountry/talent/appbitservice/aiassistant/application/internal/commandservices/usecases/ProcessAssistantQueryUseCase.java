package tech.nocountry.talent.appbitservice.aiassistant.application.internal.commandservices.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices.InclusionDataPort;
import tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices.PromptBuilderService;
import tech.nocountry.talent.appbitservice.aiassistant.domain.model.commands.AssistantQueryCommand;
import tech.nocountry.talent.appbitservice.aiassistant.domain.model.valueobjects.AssistantReply;
import tech.nocountry.talent.appbitservice.aiassistant.infrastructure.llm.OpenRouterChatClient;

/**
 * Use case for processing AI assistant queries from government managers.
 * Orchestrates fetching inclusion data, building prompts, and calling the LLM.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessAssistantQueryUseCase {
    private final InclusionDataPort inclusionDataPort;
    private final PromptBuilderService promptBuilderService;
    private final OpenRouterChatClient llmClient;

    /**
     * Processes a query from a government manager.
     *
     * @param command the query command containing the question and optional context
     * @return the AI assistant's response
     */
    @Transactional(readOnly = true)
    public AssistantReply execute(AssistantQueryCommand command) {
        log.info("Processing assistant query: {}", command.question());

        var inclusionData = inclusionDataPort.getDataForQuery(command.question());
        log.debug("Fetched inclusion data for query");

        var systemPrompt = promptBuilderService.buildSystemPrompt(inclusionData);
        var history = promptBuilderService.buildHistory(command);
        var userQuestion = promptBuilderService.buildUserQuestion(command);
        log.debug("Built structured prompt with inclusion data");

        var llmResponse = llmClient.chat(systemPrompt, history, userQuestion);
        log.info("Received response from LLM");

        var sources = determineSources(command.question());

        return AssistantReply.create(
                llmResponse.content(),
                sources,
                llmResponse.model(),
                llmResponse.totalTokens()
        );
    }

    private java.util.List<String> determineSources(String question) {
        String lower = question.toLowerCase();
        java.util.List<String> sources = new java.util.ArrayList<>();

        if (lower.contains("vulnerable") || lower.contains("region") || lower.contains("brecha") || lower.contains("gap") || lower.contains("digital")) {
            sources.add("Vulnerable Regions Data");
        }
        if (lower.contains("mental") || lower.contains("health") || lower.contains("salud")) {
            sources.add("Mental Health Reports");
        }
        if (lower.contains("employment") || lower.contains("empleo") || lower.contains("employability") || lower.contains("labor")) {
            sources.add("Employability Data");
        }

        if (sources.isEmpty()) {
            sources.add("All Available Data");
        }

        return sources;
    }
}
