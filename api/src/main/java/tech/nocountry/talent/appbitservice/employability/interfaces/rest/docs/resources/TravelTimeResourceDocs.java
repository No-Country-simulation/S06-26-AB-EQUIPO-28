package tech.nocountry.talent.appbitservice.employability.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for {@link tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.TravelTimeResource}.
 */
public final class TravelTimeResourceDocs {
    private TravelTimeResourceDocs() {}

    public static final String DESCRIPTION = """
            Registro de tiempos de viaje inter-cluster del dataset CDRView para el BC employability. \
            Agrega observaciones de desplazamiento entre dos clusters: número de observaciones, \
            distancia promedio y percentiles 25/75, junto con el período de sesión predominante.""";
    public static final String ORIGIN_CLUSTER = "Cluster geográfico de origen.";
    public static final String DESTINATION_CLUSTER = "Cluster geográfico de destino.";
    public static final String SAME_CLUSTER = "Indica si origen y destino son el mismo cluster.";
    public static final String OBSERVATIONS = "Número de observaciones de viaje registradas.";
    public static final String AVERAGE_DISTANCE_KM = "Distancia promedio de viaje en km.";
    public static final String P25_DISTANCE_KM = "Percentil 25 de la distancia de viaje en km. Admite null.";
    public static final String P75_DISTANCE_KM = "Percentil 75 de la distancia de viaje en km. Admite null.";
    public static final String PREDOMINANT_PERIOD = "Período predominante: DAWN, MORNING, AFTERNOON, NIGHT.";
}
