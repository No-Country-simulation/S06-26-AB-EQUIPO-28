package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramPaginatedResource}.
 *
 * <p>Centralizes the field descriptions of the paginated response to keep them
 * consistent and maintainable, mirroring {@code AntennaPaginatedResourceDocs}.</p>
 */
public final class MentorshipProgramPaginatedResourceDocs {

    private MentorshipProgramPaginatedResourceDocs() {}

    public static final String DESCRIPTION =
            "Respuesta paginada con la lista de programas de mentoría del catálogo público. "
            + "Incluye el contenido de la página actual y los metadatos de paginación.";
    public static final String CONTENT = "Lista de programas de mentoría incluidos en la página actual.";
    public static final String TOTAL_ELEMENTS =
            "Total de programas que coinciden con los criterios de búsqueda (en todas las páginas). Valor entero >= 0.";
    public static final String CURRENT_PAGE =
            "Número de la página actual, indexado desde 0 (0-based). Coincide con el parámetro 'page' enviado.";
    public static final String PAGE_SIZE =
            "Cantidad de elementos por página. Coincide con el parámetro 'size' enviado (entre 1 y 100; por defecto 20).";
    public static final String TOTAL_PAGES =
            "Total de páginas disponibles. Se calcula como: ceil(totalElements / pageSize).";
}
