package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

/**
 * Query para analizar brechas de mentoría: clusters con alta vulnerabilidad
 * pero sin cobertura de programas de mentoría.
 *
 * <p>Esta query orquesta el cruce entre el catálogo de mentorías (este BC) y
 * los índices de vulnerabilidad del BC inclusion-core. La ejecución concreta
 * se hace en el query service que consume un ACL contra inclusion-core.</p>
 *
 * @param minVulnerabilityScore umbral mínimo de índice de vulnerabilidad (0-100)
 * @param maxResults            número máximo de clusters a devolver (1-1000)
 * @param focusArea             área de enfoque para filtrar brechas (nullable = todas)
 */
public record GetMentorshipGapsQuery(
        int minVulnerabilityScore,
        int maxResults,
        String focusArea
) {
    private static final int DEFAULT_MIN_SCORE = 60;
    private static final int DEFAULT_MAX_RESULTS = 20;
    private static final int SCORE_MIN = 0;
    private static final int SCORE_MAX = 100;
    private static final int RESULTS_MIN = 1;
    private static final int RESULTS_MAX = 1000;

    /**
     * Constructor compact: valida los rangos de los parámetros.
     *
     * @throws IllegalArgumentException si {@code minVulnerabilityScore} está fuera
     *         de [0, 100] o {@code maxResults} está fuera de [1, 1000]
     */
    public GetMentorshipGapsQuery {
        if (minVulnerabilityScore < SCORE_MIN || minVulnerabilityScore > SCORE_MAX) {
            throw new IllegalArgumentException(
                    String.format("minVulnerabilityScore debe estar entre %d y %d", SCORE_MIN, SCORE_MAX));
        }
        if (maxResults < RESULTS_MIN || maxResults > RESULTS_MAX) {
            throw new IllegalArgumentException(
                    String.format("maxResults debe estar entre %d y %d", RESULTS_MIN, RESULTS_MAX));
        }
    }

    /**
     * Factory con valores por defecto: umbral de vulnerabilidad 60, 20 resultados,
     * sin filtro de área de enfoco.
     *
     * @return query por defecto
     */
    public static GetMentorshipGapsQuery withDefaults() {
        return new GetMentorshipGapsQuery(DEFAULT_MIN_SCORE, DEFAULT_MAX_RESULTS, null);
    }
}
