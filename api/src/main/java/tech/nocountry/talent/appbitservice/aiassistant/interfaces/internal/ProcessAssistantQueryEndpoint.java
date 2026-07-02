package tech.nocountry.talent.appbitservice.aiassistant.interfaces.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.aiassistant.application.internal.commandservices.usecases.ProcessAssistantQueryUseCase;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantQueryResource;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantResponseResource;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.transform.AssistantQueryAssembler;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.transform.AssistantResponseAssembler;

@Component
@RequiredArgsConstructor
public class ProcessAssistantQueryEndpoint {
    private final ProcessAssistantQueryUseCase useCase;
    private final AssistantQueryAssembler queryAssembler;
    private final AssistantResponseAssembler responseAssembler;

    public AssistantResponseResource processQuery(AssistantQueryResource resource) {
        var command = queryAssembler.toCommand(resource);
        var reply = useCase.execute(command);
        return responseAssembler.toResource(reply);
    }
}
