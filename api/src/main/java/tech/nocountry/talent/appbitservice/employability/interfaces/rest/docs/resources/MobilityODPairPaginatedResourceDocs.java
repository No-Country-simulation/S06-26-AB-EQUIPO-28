package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairPaginatedResource}.
 *
 * <p>Mismas descripciones que {@code MentorshipProgramPaginatedResourceDocs} adaptadas al dominio
 * de movilidad OD.</p>
 */
public final class MobilityODPairPaginatedResourceDocs {
    private MobilityODPairPaginatedResourceDocs() {}

    public static final String DESCRIPTION = """
            Resultado paginado de pares origen-destino de movilidad. Envuelve una lista de \
            MobilityODPairResource con metadatos de paginación, permitiendo navegar grandes \
            volúmenes del catálogo OD de CDRView.""";
    public static final String CONTENT = "Lista de pares OD en la página actual.";
    public static final String TOTAL_ELEMENTS = "Total de elementos que coinciden con la query (todas las páginas).";
    public static final String CURRENT_PAGE = "Número de página actual (0-based).";
    public static final String PAGE_SIZE = "Cantidad de elementos por página.";
    public static final String TOTAL_PAGES = "Cantidad total de páginas disponibles.";
}
