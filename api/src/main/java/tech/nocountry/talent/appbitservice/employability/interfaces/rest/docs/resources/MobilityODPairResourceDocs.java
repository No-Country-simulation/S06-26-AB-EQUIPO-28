package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.MobilityODPairResource}.
 *
 * <p>Provides reusable static string constants for {@code @Schema} descriptions,
 * following the same pattern as {@code MentorshipProgramResourceDocs}.</p>
 */
public final class MobilityODPairResourceDocs {
    private MobilityODPairResourceDocs() {}

    public static final String DESCRIPTION = """
            Par origen-destino (OD) de movilidad del dataset CDRView para el BC employability. \
            Cada registro describe el flujo observado entre dos clusters geográficos de la \
            Región Metropolitana de Florianópolis: usuarios únicos, viajes totales, distancia \
            promedio (Haversine) y período de sesión predominante.""";
    public static final String ORIGIN_CLUSTER = "Cluster geográfico de origen.";
    public static final String ORIGIN_MUNICIPIO = "Municipio de origen. Admite null.";
    public static final String ORIGIN_LATITUDE = "Latitud del punto de origen (WGS84).";
    public static final String ORIGIN_LONGITUDE = "Longitud del punto de origen (WGS84).";
    public static final String DESTINATION_CLUSTER = "Cluster geográfico de destino.";
    public static final String DESTINATION_MUNICIPIO = "Municipio de destino. Admite null.";
    public static final String DESTINATION_LATITUDE = "Latitud del punto de destino (WGS84).";
    public static final String DESTINATION_LONGITUDE = "Longitud del punto de destino (WGS84).";
    public static final String SAME_CLUSTER = "Indica si origen y destino son el mismo cluster.";
    public static final String UNIQUE_USERS = "Usuarios únicos que recorrieron el par OD.";
    public static final String TOTAL_TRIPS = "Viajes totales observados en el par OD.";
    public static final String AVERAGE_DISTANCE_KM = "Distancia promedio recorrida en km (Haversine).";
    public static final String PREDOMINANT_PERIOD = "Período predominante: DAWN, MORNING, AFTERNOON, NIGHT.";
    public static final String CREATED_AT = "Fecha de creación del registro.";
    public static final String UPDATED_AT = "Fecha de última actualización.";
}
