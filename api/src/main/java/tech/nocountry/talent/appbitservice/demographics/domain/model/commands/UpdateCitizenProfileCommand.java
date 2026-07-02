package tech.nocountry.talent.appbitservice.demographics.domain.model.commands;

import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.*;

/**
 * Command para actualizar un perfil de ciudadano existente.
 *
 * <p>Contiene los datos actualizados para un CitizenProfile.</p>
 *
 * @param citizenId identificador único del ciudadano
 * @param incomeLevel nuevo nivel de ingreso económico (puede ser null para no actualizar)
 * @param ageGroup nuevo grupo de edad (puede ser null para no actualizar)
 * @param mobilityPattern nuevo patrón de movilidad (puede ser null para no actualizar)
 * @param homeCluster nuevo cluster geográfico de residencia (puede ser null para no actualizar)
 */
public record UpdateCitizenProfileCommand(
        CitizenId citizenId,
        IncomeLevel incomeLevel,
        AgeGroup ageGroup,
        MobilityPattern mobilityPattern,
        HomeCluster homeCluster
) { }