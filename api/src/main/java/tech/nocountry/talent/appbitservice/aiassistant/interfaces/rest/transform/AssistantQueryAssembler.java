package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.aiassistant.domain.model.commands.AssistantQueryCommand;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantQueryResource;

@Mapper(componentModel = "spring")
public interface AssistantQueryAssembler {
    AssistantQueryCommand toCommand(AssistantQueryResource resource);
}
