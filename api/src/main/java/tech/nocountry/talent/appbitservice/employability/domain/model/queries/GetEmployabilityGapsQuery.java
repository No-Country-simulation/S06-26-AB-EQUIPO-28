package tech.nocountry.talent.appbitservice.employability.domain.model.queries;

/**
 * Query para analizar brechas de empleabilidad: clusters con alta densidad de
 * ciudadanos en horario laboral pero con baja conectividad saliente hacia los
 * hubs de empleo.
 *
 * <p>Esta query orquesta el cruce entre la matriz OD de movilidad (este BC) y
 * los índices demográficos del BC inclusion-core/demographics. La ejecución
 * concreta se hace en el query service que consume un ACL contra
 * inclusion-core.</p>
 *
 * @param maxResults     número máximo de clusters a devolver (1-1000)
 * @param minSeverity    severidad mínima de la brecha (CRITICAL, HIGH, MODERATE, LOW, NONE) (nullable = todas)
 * @param cluster        filtro por cluster específico (nullable)
 * @param onlyBlindZones si {@code true}, solo clusters sin cobertura de telemetría
 */
public record GetEmployabilityGapsQuery(
        int maxResults,
        String minSeverity,
        String cluster,
        boolean onlyBlindZones
) {
    private static final int DEFAULT_MAX_RESULTS = 20;
    private static final int RESULTS_MIN = 1;
    private static final int RESULTS_MAX = 1000;

    /**
     * Constructor compact: valida el rango de {@code maxResults}.
     *
     * @throws IllegalArgumentException si {@code maxResults} está fuera de [1, 1000]
     */
    public GetEmployabilityGapsQuery {
        if (maxResults < RESULTS_MIN || maxResults > RESULTS_MAX) {
            throw new IllegalArgumentException(
                    String.format("maxResults debe estar entre %d y %d", RESULTS_MIN, RESULTS_MAX));
        }
    }

    /**
     * Factory con valores por defecto: 20 resultados, sin umbral de severidad,
     * sin filtro de cluster, sin restricción de blind zones.
     *
     * @return query por defecto
     */
    public static GetEmployabilityGapsQuery withDefaults() {
        return new GetEmployabilityGapsQuery(DEFAULT_MAX_RESULTS, null, null, false);
    }
}