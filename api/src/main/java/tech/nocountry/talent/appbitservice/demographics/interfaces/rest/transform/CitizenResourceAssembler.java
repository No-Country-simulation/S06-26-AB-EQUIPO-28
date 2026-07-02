package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.transform;

import org.mapstruct.*;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.CreateCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.DeleteCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.UpdateCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.*;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CreateCitizenResource;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.UpdateCitizenResource;

/**
 * MapStruct assembler que convierte recursos REST (DTOs) a comandos de dominio.
 *
 * <p>Utiliza métodos {@code @Named} para transformar Strings planos a Value Objects
 * del dominio ({@link CitizenId}, {@link IncomeLevel}, etc.).</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CitizenResourceAssembler {

    @Mapping(target = "citizenId", source = "citizenHash", qualifiedByName = "toCitizenId")
    @Mapping(target = "incomeLevel", source = "incomeLevel", qualifiedByName = "toIncomeLevel")
    @Mapping(target = "ageGroup", source = "ageGroup", qualifiedByName = "toAgeGroup")
    @Mapping(target = "mobilityPattern", source = "mobilityPattern", qualifiedByName = "toMobilityPattern")
    @Mapping(target = "homeCluster", source = "homeCluster", qualifiedByName = "toHomeCluster")
    CreateCitizenProfileCommand toCreateCommand(CreateCitizenResource resource);

    @Mapping(target = "citizenId", source = "citizenHash", qualifiedByName = "toCitizenId")
    @Mapping(target = "incomeLevel", source = "resource.incomeLevel", qualifiedByName = "toIncomeLevel")
    @Mapping(target = "ageGroup", source = "resource.ageGroup", qualifiedByName = "toAgeGroup")
    @Mapping(target = "mobilityPattern", source = "resource.mobilityPattern", qualifiedByName = "toMobilityPattern")
    @Mapping(target = "homeCluster", source = "resource.homeCluster", qualifiedByName = "toHomeCluster")
    UpdateCitizenProfileCommand toUpdateCommand(String citizenHash, UpdateCitizenResource resource);

    @Mapping(target = "citizenId", source = "citizenHash", qualifiedByName = "toCitizenId")
    DeleteCitizenProfileCommand toDeleteCommand(String citizenHash);

    // Conversores de String a Value Objects del dominio
    @Named("toCitizenId")
    default CitizenId toCitizenId(String value) {
        return value != null ? CitizenId.of(value) : null;
    }

    @Named("toIncomeLevel")
    default IncomeLevel toIncomeLevel(String value) {
        return value != null ? IncomeLevel.of(value) : null;
    }

    @Named("toAgeGroup")
    default AgeGroup toAgeGroup(String value) {
        return value != null ? AgeGroup.of(value) : null;
    }

    @Named("toMobilityPattern")
    default MobilityPattern toMobilityPattern(String value) {
        return value != null ? MobilityPattern.of(value) : null;
    }

    @Named("toHomeCluster")
    default HomeCluster toHomeCluster(String value) {
        return value != null ? HomeCluster.of(value) : null;
    }
}
