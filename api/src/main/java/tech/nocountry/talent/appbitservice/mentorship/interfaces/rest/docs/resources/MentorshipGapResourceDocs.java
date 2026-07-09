package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipGapResource}.
 */
public final class MentorshipGapResourceDocs {
    private MentorshipGapResourceDocs() {}

    public static final String DESCRIPTION = """
            Brecha de mentoría en un cluster vulnerable: un cluster geográfico con alta vulnerabilidad \
            social pero cobertura insuficiente de programas de mentoría. Identifica dónde se necesitan \
            más intervenciones según el puntaje de vulnerabilidad y la densidad de programas existentes.""";
    public static final String CLUSTER_NAME = "Cluster geográfico del dataset CDRView.";
    public static final String VULNERABILITY_SCORE = "Puntaje de vulnerabilidad (0-100). Mayor = más vulnerable.";
    public static final String VULNERABLE_POPULATION_COUNT = "Población vulnerable (ingreso D) en el cluster.";
    public static final String CONNECTIVITY_LEVEL = "Nivel de conectividad: HIGH, MEDIUM, LOW.";
    public static final String HAS_MENTORSHIP_PROGRAMS = "Indica si el cluster tiene programas de mentoría.";
    public static final String MATCHING_PROGRAMS = "Programas de mentoría existentes en el cluster (puede estar vacía).";
    public static final String GAP_SEVERITY = "Severidad de la brecha: CRITICAL, HIGH o MODERATE.";
}