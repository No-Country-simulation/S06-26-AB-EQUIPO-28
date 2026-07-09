package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

/**
 * Query para enumerar los clusters geográficos que aparecen en el catálogo de
 * mentorías.
 *
 * <p>Record vacío: la query no recibe parámetros. Existe como record (y no como
 * simple llamada al query service) para mantener simetría CQRS con las demás
 * queries del BC y facilitar logging/observabilidad (la query queda traceable
 * en el pipeline como cualquier otra).</p>
 *
 * <p>Filtro opcional por área de enfoco: si se quiere listar solo los clusters
 * que tienen programas con un área de enfoco concreta, usar el factory
 * {@link #withFocusArea(String)}.</p>
 */
public record GetMentorshipClustersQuery(
        String focusArea
) {
    /**
     * Factory por defecto: lista todos los clusters del catálogo sin filtro.
     *
     * @return query sin filtro de área
     */
    public static GetMentorshipClustersQuery withDefaults() {
        return new GetMentorshipClustersQuery(null);
    }

    /**
     * Factory para filtrar clusters por área de enfoco.
     *
     * @param focusArea área de enfoco (TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL);
     *                  {@code null} o blank para no filtrar
     * @return query con filtro de área
     */
    public static GetMentorshipClustersQuery withFocusArea(String focusArea) {
        return new GetMentorshipClustersQuery(focusArea);
    }
}
