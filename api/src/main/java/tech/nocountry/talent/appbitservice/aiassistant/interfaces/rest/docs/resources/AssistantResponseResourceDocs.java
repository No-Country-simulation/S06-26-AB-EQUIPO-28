package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources;

public final class AssistantResponseResourceDocs {
    private AssistantResponseResourceDocs() {}

    public static final String DESCRIPTION =
            "Respuesta del asistente de IA. Contiene el análisis ejecutivo en lenguaje natural, "
            + "las fuentes de datos consultadas y metadatos de la generación.";

    public static final String ANSWER =
            "Respuesta en lenguaje natural generada por el modelo, normalmente en formato Markdown, "
            + "con análisis, hallazgos y recomendaciones de política pública basados en los "
            + "indicadores cargados para la pregunta.";

    public static final String SOURCES =
            "Fuentes de datos utilizadas para construir la respuesta, derivadas de la pregunta. "
            + "Valores posibles: 'Vulnerable Regions Data', 'Mental Health Reports', "
            + "'Employability Data' o 'All Available Data' cuando no se detecta un tema específico.";

    public static final String METADATA =
            "Metadatos de la generación: modelo de IA empleado y consumo de tokens.";

    public static final String TIMESTAMP =
            "Momento en que se generó la respuesta, en formato ISO-8601 UTC (ej. "
            + "2026-06-24T05:41:17.973Z).";

    public static final String METADATA_MODEL =
            "Identificador del modelo de IA utilizado (proveedor OpenRouter). "
            + "Ej.: nvidia/nemotron-3-ultra-550b-a55b:free.";

    public static final String METADATA_TOKENS =
            "Total de tokens consumidos en la generación (prompt + respuesta). Entero >= 0; "
            + "0 si el proveedor no reporta uso.";
}
