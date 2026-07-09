package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.EmployabilityGapResource}.
 */
public final class EmployabilityGapResourceDocs {
    private EmployabilityGapResourceDocs() {}

    public static final String DESCRIPTION = """
            Brecha de empleabilidad en un cluster geográfico: un cluster con alta densidad de \
            ciudadanos en horario laboral pero baja conectividad saliente hacia los hubs de \
            empleo. Resultado del cruce entre la matriz OD de movilidad (este BC), los índices \
            demográficos (ACL demographics) y la telemetría diurna (ACL telemetry). Ordenado por \
            gapScore descendente.""";
    public static final String CLUSTER = "Cluster geográfico del dataset CDRView.";
    public static final String MUNICIPALITIES = "Municipios que componen el cluster.";
    public static final String CITIZEN_COUNT = "Total de ciudadanos en el cluster.";
    public static final String INCOME_D_COUNT = "Ciudadanos en nivel de renta D (baja renta).";
    public static final String INCOME_C_COUNT = "Ciudadanos en nivel de renta C.";
    public static final String YOUTH_COUNT_18_24 = "Jóvenes de 18 a 24 años en el cluster.";
    public static final String HAS_TELEMETRY_COVERAGE = "Indica si el cluster tiene cobertura de telemetría reportada.";
    public static final String DAYTIME_AVG_USERS = "Promedio de usuarios activos en horario laboral (MORNING + AFTERNOON).";
    public static final String OUTBOUND_TRIPS_TO_HUBS = "Viajes salientes hacia los hubs de empleo (TOP-5).";
    public static final String DISTANCE_TO_NEAREST_HUB_KM = "Distancia en km al hub de empleo más cercano.";
    public static final String MOBILITY_INTENSITY = "Etiqueta descriptiva de intensidad de movilidad.";
    public static final String GAP_SEVERITY = "Severidad de la brecha: CRITICAL, HIGH, MODERATE, LOW, NONE.";
    public static final String GAP_SCORE = "Puntaje agregado de la brecha (0-100). Mayor = más crítica.";
    public static final String PRIMARY_FACTORS = "Lista de factores que dispararon el cálculo (trazabilidad explicativa).";
}
