package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipClusterSummaryResource}.
 */
public final class MentorshipClusterResourceDocs {
    private MentorshipClusterResourceDocs() {}

    public static final String DESCRIPTION = """
            Resumen de cobertura de mentoría en un cluster geográfico. Agrega todos los programas \
            que operan en el cluster, proporcionando conteos, capacidades, modalidades y detalles \
            de público objetivo para los gestores públicos.""";
    public static final String CLUSTER_NAME = "Cluster geográfico del dataset CDRView.";
    public static final String TOTAL_PROGRAMS = "Total de programas en el cluster.";
    public static final String ACTIVE_PROGRAMS = "Programas activos en el cluster.";
    public static final String FOCUS_AREAS = "Áreas de enfoque cubiertas en el cluster.";
    public static final String MODALITIES = "Modalidades disponibles.";
    public static final String TARGET_AUDIENCES = "Públicos objetivo atendidos.";
    public static final String TOTAL_CAPACITY = "Capacidad total combinada de todos los programas del cluster.";
    public static final String TOTAL_ACTIVE_MENTEES = "Mentorados activos totales en todos los programas.";
    public static final String PROGRAMS = "Lista de programas en el cluster.";
}