package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources;

/**
 * Constantes de documentación OpenAPI para {@link tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource}.
 *
 * <p>Centraliza las descripciones de los campos de la respuesta paginada para mantener
 * consistencia y facilitar su mantenimiento.</p>
 */
public final class AntennaPaginatedResourceDocs {

    private AntennaPaginatedResourceDocs() {}

    public static final String DESCRIPTION =
            "Respuesta paginada con la lista de antenas (ERB / celdas) de la red móvil. "
            + "Incluye el contenido de la página actual y los metadatos de paginación.";
    public static final String CONTENT = "Lista de antenas incluidas en la página actual.";
    public static final String TOTAL_ELEMENTS =
            "Total de antenas que coinciden con los criterios de búsqueda (en todas las páginas). Valor entero >= 0.";
    public static final String CURRENT_PAGE =
            "Número de la página actual, indexado desde 0 (0-based). Coincide con el parámetro 'page' enviado.";
    public static final String PAGE_SIZE =
            "Cantidad de elementos por página. Coincide con el parámetro 'size' enviado (entre 1 y 100; por defecto 20).";
    public static final String TOTAL_PAGES =
            "Total de páginas disponibles. Se calcula como: ceil(totalElements / pageSize).";
}
