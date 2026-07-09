package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimePaginatedResource}.
 */
public final class TravelTimePaginatedResourceDocs {
    private TravelTimePaginatedResourceDocs() {}

    public static final String DESCRIPTION = """
            Resultado paginado de registros de tiempos de viaje inter-cluster. Envuelve una \
            lista de TravelTimeResource con metadatos de paginación.""";
    public static final String CONTENT = "Lista de registros de tiempos de viaje en la página actual.";
    public static final String TOTAL_ELEMENTS = "Total de elementos que coinciden con la query (todas las páginas).";
    public static final String CURRENT_PAGE = "Número de página actual (0-based).";
    public static final String PAGE_SIZE = "Cantidad de elementos por página.";
    public static final String TOTAL_PAGES = "Cantidad total de páginas disponibles.";
}
