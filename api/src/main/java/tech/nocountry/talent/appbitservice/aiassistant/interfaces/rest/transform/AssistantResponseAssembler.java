package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.aiassistant.domain.model.valueobjects.AssistantReply;
import tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.resources.AssistantResponseResource;

@Mapper(componentModel = "spring")
public interface AssistantResponseAssembler {
    AssistantResponseResource toResource(AssistantReply reply);
}
