package tech.nocountry.talent.appbitservice.employability.domain.model.queries;

/**
 * Query paginada y filtrada del catálogo de pares origen-destino de movilidad.
 *
 * <p>Record inmutable que encapsula los parámetros de búsqueda. Los filtros son
 * opcionales excepto {@code page} y {@code size} (con validación de rango en el
 * constructor compact). El {@code sort} se expresa como {@code "field,direction"}
 * (e.g., {@code "createdAt,desc"}) y se traduce a Spring Data {@code Pageable}
 * en el query service.</p>
 *
 * @param page               índice de página (0-based)
 * @param size               tamaño de página (1-100)
 * @param sort               criterio de ordenación ("field,direction")
 * @param originCluster      filtro por cluster de origen (nullable)
 * @param destinationCluster filtro por cluster de destino (nullable)
 * @param predominantPeriod  filtro por período predominante (nullable)
 */
public record GetMobilityODPairsQuery(
        int page,
        int size,
        String sort,
        String originCluster,
        String destinationCluster,
        String predominantPeriod
) {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT = "createdAt,desc";

    /**
     * Constructor compact: valida los rangos de paginación.
     *
     * @throws IllegalArgumentException si {@code page < 0} o {@code size} está
     *         fuera de [1, 100]
     */
    public GetMobilityODPairsQuery {
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
    public static GetMobilityODPairsQuery withDefaults() {
        return new GetMobilityODPairsQuery(
                DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT, null, null, null);
    }

    /**
     * Factory con filtros explícitos y paginación personalizada.
     *
     * @param page               índice de página (>= 0)
     * @param size               tamaño de página (1-100)
     * @param sort               criterio de ordenación (nullable → default)
     * @param originCluster      filtro por cluster de origen (nullable)
     * @param destinationCluster filtro por cluster de destino (nullable)
     * @param predominantPeriod  filtro por período predominante (nullable)
     * @return query validada
     */
    public static GetMobilityODPairsQuery withFilters(
            int page, int size, String sort,
            String originCluster, String destinationCluster, String predominantPeriod) {
        String resolvedSort = (sort == null || sort.isBlank()) ? DEFAULT_SORT : sort;
        return new GetMobilityODPairsQuery(
                page, size, resolvedSort,
                originCluster, destinationCluster, predominantPeriod);
    }
}