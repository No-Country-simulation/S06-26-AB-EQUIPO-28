package tech.nocountry.talent.appbitservice.mentorship.domain.exceptions;

import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.ErrorCodes;

/**
 * Excepción lanzada cuando no se encuentra un programa de mentoría por su
 * identificador de negocio ({@code program_id}, e.g., "MPR-001").
 */
public class MentorshipProgramNotFoundException extends MentorshipDomainException {

    /**
     * Construye la excepción indicando el {@code programId} no encontrado.
     *
     * @param programId identificador de negocio del programa de mentoría
     */
    public MentorshipProgramNotFoundException(String programId) {
        super(ErrorCodes.MEN_PROGRAM_NOT_FOUND,
                String.format("Programa de mentoría no encontrado: %s", programId));
    }

    /**
     * Construye la excepción con {@code programId} y causa raíz.
     *
     * @param programId identificador de negocio del programa de mentoría
     * @param cause     causa raíz
     */
    public MentorshipProgramNotFoundException(String programId, Throwable cause) {
        super(ErrorCodes.MEN_PROGRAM_NOT_FOUND,
                String.format("Programa de mentoría no encontrado: %s", programId),
                cause);
    }
}
