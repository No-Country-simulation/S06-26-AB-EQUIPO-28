package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources;

/**
 * Constantes de documentación OpenAPI para {@code CitizenResource}.
 *
 * <p>Centraliza las descripciones de los campos del recurso de ciudadano
 * para mantener consistencia y facilitar el mantenimiento.</p>
 */
public final class CitizenDocs {
    private CitizenDocs() { }

    public static final String CITIZEN_HASH =
            "Identificador único anonimizado del ciudadano/abonado (string). "
            + "Equivale al campo assinante_hash del CDR. No contiene datos personales. "
            + "Al crear es obligatorio y debe tener entre 32 y 64 caracteres.";

    public static final String INCOME_LEVEL =
            "Nivel de ingresos (faja de renta) del ciudadano. Valor de 1 carácter. "
            + "Valores permitidos: A (alta), B (media-alta), C (media), D (baja). "
            + "El nivel D corresponde a la población VULNERABLE y es el que utiliza el contexto "
            + "inclusion-core para calcular la vulnerabilidad. Se normaliza a mayúsculas.";

    public static final String AGE_GROUP =
            "Grupo etario (faja de edad) del ciudadano. "
            + "Valores permitidos exactos: 18-24, 25-34, 35-44, 45-54, 55+. "
            + "Se usa para segmentar el tipo de soporte social necesario.";

    public static final String MOBILITY_PATTERN =
            "Patrón de movilidad según la cantidad de antenas distintas que visita por día. "
            + "Valores permitidos: LOW (baja, 1-2 antenas/día; permanece cerca del hogar), "
            + "MODERATE (moderada, 2-4 antenas/día), INTENSE (intensa, 4-8 antenas/día; "
            + "se desplaza frecuentemente entre clusters). Se normaliza a mayúsculas.";

    public static final String HOME_CLUSTER =
            "Zona geográfica de residencia probable (uno de los 27 clusters de la Región "
            + "Metropolitana de Florianópolis). Texto libre de máximo 50 caracteres. "
            + "Ejemplos: CBD_BEIRAMAR, CENTRO_HISTORICO, SAO_JOSE_CENTRO, TRINDADE, COQUEIROS, "
            + "UFSC, CAMPECHE, LAGOA_CONCEICAO.";

    public static final String RECORD_DESCRIPTION =
            "Perfil demográfico de un ciudadano/abonado sintético. Estos datos se cruzan con la "
            + "telemetría de red para identificar regiones vulnerables y orientar políticas de "
            + "inclusión social (empleo, formación, salud mental).";

    public static final String CREATE_CITIZEN_RESOURCE =
            "Datos para crear un nuevo perfil de ciudadano. Todos los campos son obligatorios "
            + "y deben respetar los valores permitidos de cada faja.";

    public static final String UPDATE_CITIZEN_RESOURCE =
            "Datos para actualizar parcialmente un perfil de ciudadano. Todos los campos son "
            + "opcionales: solo se aplican los campos no nulos enviados. El hash del ciudadano "
            + "no se modifica (se identifica por la ruta).";
}
