package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources;

/**
 * Constantes de documentación OpenAPI para {@link tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource}.
 *
 * <p>Centraliza las descripciones de los campos de una antena (ERB / celda) de la red móvil
 * para mantener consistencia y facilitar su mantenimiento. Los datos provienen del dataset
 * de telemetría de la red móvil de la Región Metropolitana de Florianópolis (Brasil).</p>
 */
public final class AntennaResourceDocs {

    private AntennaResourceDocs() {}

    public static final String DESCRIPTION =
            "Representa una antena (ERB / celda) de la red móvil. Cada antena se identifica de forma única "
            + "por su ECGI y se ubica geográficamente mediante latitud/longitud. Estos datos alimentan los "
            + "análisis de inclusión digital (identificar dónde hay concentración de personas pero red precaria).";

    public static final String ECGI =
            "Identificador único de la celda/antena (E-UTRAN Cell Global Identifier). "
            + "Cadena de hasta 12 caracteres. IMPORTANTE: debe tratarse siempre como texto, nunca como número "
            + "(puede contener ceros a la izquierda). Ejemplo: \"530011001234\".";

    public static final String CLUSTER =
            "Zona geográfica de movilidad a la que pertenece la antena. Cadena de hasta 40 caracteres. "
            + "Existen 27 clusters calibrados por población en el área metropolitana. "
            + "Ejemplos reales: CBD_BEIRAMAR, CENTRO_HISTORICO, SAO_JOSE_CENTRO, TRINDADE, COQUEIROS, UFSC, CAMPECHE, LAGOA_CONCEICAO.";

    public static final String MUNICIPALITY =
            "Municipio real donde está instalada la ERB. Cadena de hasta 60 caracteres. "
            + "Ejemplos: Florianópolis, São José, Palhoça, Biguaçu.";

    public static final String LATITUDE =
            "Latitud geográfica de la antena en grados decimales (WGS-84). "
            + "Para la región de Florianópolis ronda valores de ~-27.x. Ejemplo: -27.5917.";

    public static final String LONGITUDE =
            "Longitud geográfica de la antena en grados decimales (WGS-84). "
            + "Para la región de Florianópolis ronda valores de ~-48.x. Ejemplo: -48.5588.";
}
