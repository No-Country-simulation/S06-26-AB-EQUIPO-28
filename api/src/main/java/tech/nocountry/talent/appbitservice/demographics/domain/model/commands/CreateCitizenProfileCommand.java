package tech.nocountry.talent.appbitservice.demographics.domain.model.commands;

import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.*;

/**
 * Command para crear un nuevo perfil de ciudadano.
 *
 * <p>Contiene los datos necesarios para crear un CitizenProfile.</p>
 *
 * @param citizenId identificador único del ciudadano
 * @param incomeLevel nivel de ingreso económico
 * @param ageGroup grupo de edad
 * @param mobilityPattern patrón de movilidad
 * @param homeCluster cluster geográfico de residencia
 */
public record CreateCitizenProfileCommand(
        CitizenId citizenId,
        IncomeLevel incomeLevel,
        AgeGroup ageGroup,
        MobilityPattern mobilityPattern,
        HomeCluster homeCluster
) { }