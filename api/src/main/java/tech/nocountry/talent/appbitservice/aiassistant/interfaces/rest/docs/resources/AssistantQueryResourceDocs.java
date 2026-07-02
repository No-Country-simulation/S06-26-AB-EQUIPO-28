package tech.nocountry.talent.appbitservice.aiassistant.interfaces.rest.docs.resources;

public final class AssistantQueryResourceDocs {
    private AssistantQueryResourceDocs() { }

    public static final String DESCRIPTION =
            "Consulta en lenguaje natural dirigida al asistente de IA para gestores públicos. "
            + "El sistema detecta automáticamente, a partir de las palabras de la pregunta, qué "
            + "indicadores consultar (regiones vulnerables, salud mental o empleabilidad) y los "
            + "incluye como contexto antes de enviar la pregunta al modelo de lenguaje.";

    public static final String QUESTION =
            "Pregunta en lenguaje natural del gestor público. Campo OBLIGATORIO y no vacío "
            + "(@NotBlank). Las palabras clave determinan qué datos se cargan: términos como "
            + "'vulnerable', 'región', 'brecha', 'gap' o 'digital' cargan regiones vulnerables; "
            + "'mental', 'health' o 'salud' cargan el reporte de salud mental; 'empleo', "
            + "'employment' o 'labor' cargan datos de empleabilidad. Si no coincide ninguna "
            + "palabra clave, se cargan todos los indicadores como contexto general. "
            + "Ej.: \"¿Cuáles son las regiones más vulnerables?\"";

    public static final String CONTEXT =
            "Lista OPCIONAL de parámetros de contexto (texto libre) que se anteponen a la "
            + "pregunta para acotar la consulta, p. ej. una región o un período. Puede omitirse "
            + "o enviarse vacía. Ej.: [\"region: CENTRO_HISTORICO\", \"periodo: 2026-Q2\"].";

    public static final String HISTORY =
            "Historial OPCIONAL de la conversación, usado para mantener contexto en consultas "
            + "de seguimiento. Lista ordenada de mensajes previos (turnos de usuario y asistente). "
            + "Puede omitirse en la primera pregunta.";

    public static final String HISTORY_ROLE =
            "Rol del mensaje dentro del historial. Valores permitidos (sin distinción de "
            + "mayúsculas): 'user' (mensaje del gestor) o 'assistant' (respuesta previa de la IA). "
            + "Cualquier otro valor produce un error de validación. Campo obligatorio en cada entrada.";

    public static final String HISTORY_CONTENT =
            "Contenido textual del mensaje en el historial. Campo obligatorio en cada entrada.";
}
