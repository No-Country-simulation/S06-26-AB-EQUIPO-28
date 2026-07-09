package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

/**
 * Query para obtener un programa de mentoría por su identificador de negocio.
 *
 * @param programId identificador de negocio del programa (e.g., "MPR-001")
 */
public record GetMentorshipProgramByIdQuery(
        String programId
) {
    /**
     * Constructor compact: valida que {@code programId} no sea blank.
     *
     * @throws IllegalArgumentException si {@code programId} es null o blank
     */
    public GetMentorshipProgramByIdQuery {
        if (programId == null || programId.isBlank()) {
            throw new IllegalArgumentException("programId no puede estar vacío");
        }
    }

    /**
     * Factory que crea una query validada.
     *
     * @param programId identificador de negocio del programa
     * @return query validada
     */
    public static GetMentorshipProgramByIdQuery of(String programId) {
        return new GetMentorshipProgramByIdQuery(programId);
    }
}
