package tech.nocountry.talent.appbitservice.demographics.domain.model.commands;

import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.CitizenId;

/**
 * Command para eliminar un perfil de ciudadano.
 *
 * <p>Contiene el identificador del ciudadano a eliminar.</p>
 *
 * @param citizenId identificador único del ciudadano a eliminar
 */
public record DeleteCitizenProfileCommand(CitizenId citizenId) { }