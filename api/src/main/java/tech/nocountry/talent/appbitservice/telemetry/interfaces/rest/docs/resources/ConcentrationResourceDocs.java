package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources;

/**
 * Constantes de documentación OpenAPI para {@link tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource}.
 *
 * <p>Centraliza las descripciones de los campos de las métricas de concentración de red,
 * que agregan la movilidad y el uso de datos por antena, día y período del día. Estos
 * indicadores alimentan los análisis de inclusión digital (zonas con alta concentración de
 * personas pero conectividad precaria).</p>
 */
public final class ConcentrationResourceDocs {

    private ConcentrationResourceDocs() {}

    public static final String DESCRIPTION =
            "Métricas de concentración de red agregadas por antena (ECGI), día y período del día. "
            + "Reúnen volumen de usuarios, tráfico de datos, calidad de red y actividad de voz/SMS. "
            + "Sirven para detectar dónde se concentra la población y cómo se comporta la red en cada franja horaria.";

    public static final String ECGI =
            "Identificador único de la celda/antena (E-UTRAN Cell Global Identifier) a la que pertenece la métrica. "
            + "Cadena de hasta 12 caracteres; tratar siempre como texto, nunca como número. Ejemplo: \"530011001234\".";

    public static final String DAY_DATE =
            "Fecha del día al que corresponde la agregación, en formato ISO-8601 (YYYY-MM-DD). Ejemplo: 2024-03-15.";

    public static final String PERIOD =
            "Período del día al que corresponde la agregación. Valores permitidos (case-insensitive en filtros): "
            + "DAWN (madrugada, 00–06h, ~8% del uso), MORNING (mañana, 06–12h, ~28%), "
            + "AFTERNOON (tarde, 12–18h, pico de ~35%), NIGHT (noche, 18–00h, ~29%).";

    public static final String USER_COUNT =
            "Número de usuarios distintos activos en la antena durante ese día y período. "
            + "Valor entero. Rango típico observado: ~312 a ~5929.";

    public static final String SESSION_COUNT =
            "Total de sesiones de datos acumuladas en la antena durante ese día y período. Valor entero.";

    public static final String DOWNLOAD_BYTES =
            "Total de bytes descargados (downlink) en la antena durante ese día y período. "
            + "Valor entero de tipo BIGINT (puede ser muy grande).";

    public static final String UPLOAD_BYTES =
            "Total de bytes subidos (uplink) en la antena durante ese día y período. "
            + "Valor entero de tipo BIGINT (puede ser muy grande).";

    public static final String AVERAGE_DURATION_S =
            "Duración media de cada sesión de datos, expresada en segundos. Valor decimal.";

    public static final String DROP_PCT =
            "Tasa media de paquetes descartados expresada como FRACCIÓN entre 0.0 y 1.0 (ej. 0.069 = 6.9%). "
            + "NO es un porcentaje 0–100. Es un indicador de calidad de red: cuanto mayor, peor conectividad. "
            + "Rango típico observado: ~0.062 a ~0.074.";

    public static final String CONGESTION_LEVEL =
            "Nivel medio de congestión de la celda expresado como FRACCIÓN entre 0.0 y 1.0. "
            + "Cuanto mayor, más saturada está la celda. Rango típico observado: ~0.318 a ~0.381.";

    public static final String TOTAL_CALLS =
            "Total de llamadas de voz cursadas en la antena durante ese día y período. Valor entero.";

    public static final String TOTAL_MESSAGES =
            "Total de mensajes SMS cursados en la antena durante ese día y período. Valor entero.";

    public static final String LATITUDE =
            "Latitud geográfica de la antena en grados decimales (WGS-84). "
            + "Para la región de Florianópolis ronda valores de ~-27.x. Ejemplo: -27.5917.";

    public static final String LONGITUDE =
            "Longitud geográfica de la antena en grados decimales (WGS-84). "
            + "Para la región de Florianópolis ronda valores de ~-48.x. Ejemplo: -48.5588.";
}
