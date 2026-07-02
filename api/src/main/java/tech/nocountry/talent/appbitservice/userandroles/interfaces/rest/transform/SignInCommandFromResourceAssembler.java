package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.transform;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SignInCommand;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.SignInResource;

/** Ensamblador MapStruct para convertir SignInResource a SignInCommand. */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SignInCommandFromResourceAssembler {

    /** Convierte SignInResource a SignInCommand. */
    SignInCommand toCommandFromResource(SignInResource resource);
}
