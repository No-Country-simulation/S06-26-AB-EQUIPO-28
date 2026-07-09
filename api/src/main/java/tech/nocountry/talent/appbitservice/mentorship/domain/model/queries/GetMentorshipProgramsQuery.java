package tech.nocountry.talent.appbitservice.mentorship.domain.model.queries;

/**
 * Query paginada y filtrada del catálogo de programas de mentoría.
 *
 * <p>Record inmutable que encapsula los parámetros de búsqueda del catálogo
 * público. Los filtros son todos opcionales excepto {@code page} y {@code size}
 * (con validación de rango en el constructor compact). El {@code sort} se expresa
 * como {@code "field,direction"} (e.g., {@code "createdAt,desc"}) y se traduce a
 * Spring Data {@code Pageable} en el query service.</p>
 *
 * @param page               índice de página (0-based)
 * @param size               tamaño de página (1-100)
 * @param sort               criterio de ordenación ("field,direction")
 * @param focusArea          filtro por área de enfoco (nullable)
 * @param modality           filtro por modalidad (nullable)
 * @param clusterName        filtro por cluster geográfico (nullable)
 * @param targetIncomeLevel  filtro por cluster de renta (nullable)
 * @param isActive           filtro por estado activo (nullable = todos)
 */
public record GetMentorshipProgramsQuery(
        int page,
        int size,
        String sort,
        String focusArea,
        String modality,
        String clusterName,
        String targetIncomeLevel,
        Boolean isActive
) {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT = "createdAt,desc";

    /**
     * Constructor compact: valida los rangos de paginación.
     *
     * @throws IllegalArgumentException si {@code page < 0} o {@code size < 1}
     *         o {@code size > 100}
     */
    public GetMentorshipProgramsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page debe ser >= 0");
        }
        if (size < MIN_SIZE || size > MAX_SIZE) {
            throw new IllegalArgumentException(
                    String.format("size debe estar entre %d y %d", MIN_SIZE, MAX_SIZE));
        }
    }

    /**
     * Factory con valores por defecto (sin filtros, paginación estándar).
     *
     * @return query con page=0, size=20, sort="createdAt,desc", filtros null
     */
    public static GetMentorshipProgramsQuery withDefaults() {
        return new GetMentorshipProgramsQuery(
                DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT, null, null, null, null, null);
    }

    /**
     * Factory con filtros explícitos y paginación personalizada.
     *
     * @param page              índice de página (>= 0)
     * @param size              tamaño de página (1-100)
     * @param sort              criterio de ordenación (nullable → default)
     * @param focusArea         filtro por área de enfoco (nullable)
     * @param modality          filtro por modalidad (nullable)
     * @param clusterName       filtro por cluster (nullable)
     * @param targetIncomeLevel filtro por cluster de renta (nullable)
     * @param isActive          filtro por estado activo (nullable)
     * @return query validada
     */
    public static GetMentorshipProgramsQuery withFilters(
            int page, int size, String sort,
            String focusArea, String modality, String clusterName,
            String targetIncomeLevel, Boolean isActive) {
        String resolvedSort = (sort == null || sort.isBlank()) ? DEFAULT_SORT : sort;
        return new GetMentorshipProgramsQuery(
                page, size, resolvedSort,
                focusArea, modality, clusterName, targetIncomeLevel, isActive);
    }
}
