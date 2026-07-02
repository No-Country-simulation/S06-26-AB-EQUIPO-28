package tech.nocountry.talent.appbitservice.aiassistant.application.internal.outboundservices;

/**
 * Port interface for accessing Inclusion Core data.
 * This is consumed by the application layer to get data for the AI assistant.
 */
public interface InclusionDataPort {

    /**
     * Retrieves relevant inclusion data based on the query.
     *
     * @param question the user's question
     * @return inclusion data to be used in prompts
     */
    PromptBuilderService.InclusionData getDataForQuery(String question);
}
