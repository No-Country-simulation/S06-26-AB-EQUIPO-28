package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.docs.resources;

/**
 * OpenAPI documentation constants for MentalHealthReportResource.
 *
 * <p>Provides reusable string constants for {@code @Schema} descriptions
 * on the MentalHealthReportResource and its nested records fields.</p>
 */
public final class MentalHealthReportResourceDocs {
    private MentalHealthReportResourceDocs() {}

    public static final String DESCRIPTION = """
            Reporte ejecutivo de vulnerabilidad en salud mental por región para gestores públicos. \
            Agrega indicadores de vulnerabilidad poblacional cruzados con la cobertura de red: sin \
            conectividad no es viable el soporte remoto de salud mental, por lo que las regiones con \
            peor red y mayor vulnerabilidad social son las prioritarias. Los datos se calculan al \
            vuelo cruzando información demográfica y de telemetría.""";

    public static final String REPORT_ID = "Identificador único del reporte generado (UUID).";

    public static final String GENERATED_AT = """
            Fecha y hora exacta de generación del reporte, en formato ISO-8601 UTC \
            (ej. 2026-06-24T14:30:00Z).""";

    public static final String REPORT_PERIOD = """
            Período cubierto por el reporte en formato trimestral YYYY-Qn (ej. 2026-Q2). \
            Si no se solicita uno específico, corresponde al trimestre actual.""";

    public static final String REGION_SUMMARIES = """
            Lista de resúmenes de vulnerabilidad por región incluidas en el reporte, ordenadas por \
            relevancia. Si se solicitó solo prioritarias, contiene únicamente las regiones prioritarias.""";

    public static final String METADATA = """
            Metadatos agregados del reporte: totales de población, score promedio y número de \
            regiones prioritarias.""";

    public static final String SUMMARY_REGION_NAME = """
            Nombre de la zona geográfica (cluster) de la región. Ejemplos: CENTRO_HISTORICO, CBD_BEIRAMAR.""";

    public static final String SUMMARY_VULNERABILITY_SCORE = """
            Índice de vulnerabilidad de la región, entero de 0 a 100. Clasificación: 0-30 baja, \
            31-60 media, 61-100 alta. Combina proporción de población de renta baja, densidad \
            poblacional y calidad de la red.""";

    public static final String SUMMARY_VULNERABLE_POPULATION = """
            Porcentaje de población vulnerable sobre el total de la región (0-100), \
            es decir, ciudadanos de renta baja respecto a la población total.""";

    public static final String SUMMARY_TOTAL_POPULATION = "Población total de ciudadanos registrados en la región.";

    public static final String SUMMARY_CONNECTIVITY_LEVEL = """
            Nivel de conectividad de la región: HIGH, MEDIUM o LOW, derivado de la tasa de paquetes \
            descartados de la red. LOW indica brecha digital crítica que impide el soporte remoto \
            de salud mental.""";

    public static final String SUMMARY_CONCENTRATION_INDEX = """
            Índice de concentración poblacional (0-100): densidad de usuarios en las antenas de la región.""";

    public static final String SUMMARY_PRIORITY = """
            Indica si la región es prioritaria para intervención (true) cuando combina alta \
            vulnerabilidad (score >60) y conectividad LOW.""";

    public static final String META_TOTAL_VULNERABLE = """
            Población vulnerable total (ciudadanos de renta baja) sumada en todas las regiones del reporte.""";

    public static final String META_TOTAL_POPULATION = "Población total cubierta por el reporte, sumada en todas las regiones.";

    public static final String META_AVG_SCORE = """
            Score promedio de vulnerabilidad (0-100) de las regiones incluidas en el reporte.""";

    public static final String META_PRIORITY_COUNT = """
            Cantidad de regiones prioritarias para intervención (score >60 y conectividad LOW) en el reporte.""";
}
