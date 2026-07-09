package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource}
 * and {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramSummaryResource}.
 *
 * <p>Provides reusable static string constants for {@code @Schema} descriptions,
 * following the same pattern as {@code HealthVulnerabilityResourceDocs}.</p>
 */
public final class MentorshipProgramResourceDocs {
    private MentorshipProgramResourceDocs() {}

    public static final String DESCRIPTION = """
            Programa de mentoría pública registrado en el catálogo AppBit (B2G). Cada programa describe \
            una iniciativa gubernamental o comunitaria (TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL) \
            con su modalidad, público objetivo, cluster geográfico y capacidad.""";
    public static final String PROGRAM_ID = "Identificador único del programa (ej: MPR-001).";
    public static final String NAME = "Nombre del programa.";
    public static final String DESCRIPTION_FIELD = "Descripción del programa.";
    public static final String ORGANIZATION = "Organismo o institución que ofrece el programa.";
    public static final String FOCUS_AREA = "Área de enfoque: TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL.";
    public static final String MODALITY = "Modalidad: REMOTE, IN_PERSON, HYBRID.";
    public static final String TARGET_AUDIENCE = "Público objetivo: YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL. Admite null.";
    public static final String TARGET_INCOME_LEVEL = "Nivel de ingreso objetivo: A, B, C, D, ALL. Admite null.";
    public static final String CLUSTER_NAME = "Cluster geográfico del dataset CDRView.";
    public static final String TOTAL_CAPACITY = "Capacidad total de mentorados (null = ilimitada).";
    public static final String ACTIVE_MENTEES = "Mentorados activos actualmente.";
    public static final String START_DATE = "Fecha de inicio del programa. Admite null.";
    public static final String END_DATE = "Fecha de finalización del programa. Admite null.";
    public static final String IS_ACTIVE = "Indica si el programa está activo.";
    public static final String WEBSITE_URL = "URL de referencia del programa. Admite null.";
    public static final String CONTACT_EMAIL = "Email de contacto del programa. Admite null.";
    public static final String CREATED_AT = "Fecha de creación del registro.";
    public static final String UPDATED_AT = "Fecha de última actualización.";
}