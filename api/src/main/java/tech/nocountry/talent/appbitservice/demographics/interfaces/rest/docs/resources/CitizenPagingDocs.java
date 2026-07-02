package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources;

/**
 * Constantes de documentación OpenAPI para {@link tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenPaginatedResource}.
 *
 * <p>Centraliza las descripciones de los campos de la respuesta paginada para mantener
 * consistencia y facilitar su mantenimiento.</p>
 */
public final class CitizenPagingDocs {

    private CitizenPagingDocs() {}

    public static final String CONTENT =
            "Lista de ciudadanos de la página actual. Cada elemento contiene el perfil demográfico "
            + "completo (hash, nivel de ingreso, grupo etario, patrón de movilidad y cluster de residencia). "
            + "Puede estar vacía si no hay resultados para los filtros y la página solicitada.";

    public static final String TOTAL_ELEMENTS =
            "Total de ciudadanos que coinciden con los filtros aplicados (incomeLevel y/o ageGroup), "
            + "considerando todas las páginas. No es el tamaño de la página actual.";

    public static final String CURRENT_PAGE =
            "Número de la página actual, comenzando en 0 (0-based). Coincide con el parámetro 'page' solicitado.";

    public static final String PAGE_SIZE =
            "Cantidad máxima de elementos por página. Coincide con el parámetro 'size' solicitado "
            + "(valor por defecto 20, rango permitido 1-100).";

    public static final String TOTAL_PAGES =
            "Total de páginas disponibles para los filtros aplicados. Se calcula como "
            + "ceil(totalElements / pageSize).";
}
