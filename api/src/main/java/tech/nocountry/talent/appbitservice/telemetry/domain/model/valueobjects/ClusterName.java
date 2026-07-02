package tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.text.Normalizer;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Value Object que representa el nombre de un cluster geográfico.
 *
 * <p>Los clusters son zonas funcionales de la Región Metropolitana de Florianópolis.
 * Existen 27 valores válidos predefinidos.</p>
 *
 * <p>Este es un {@code @Embeddable} de JPA.</p>
 *
 * @param value el nombre del cluster
 */
@Embeddable
public record ClusterName(
        @Column(name = "cluster", nullable = false, length = 50)  // CSV: cluster
        String value
) {
    // Conjuntos de 27 valores válidos de clusters de la RM de Florianópolis
    public static final Set<String> VALID_CLUSTERS = Set.of(
            "CBD_BEIRAMAR",
            "CENTRO_HISTORICO",
            "TRINDADE",
            "UFSC",
            "COQUEIROS",
            "ESTREITO_CAPOEIRAS",
            "AEROPORTO_HLZ",
            "CAMPECHE",
            "LAGOA_CONCEICAO",
            "JURERE",
            "CANASVIEIRAS",
            "INGLESES",
            "NORTE_ILHA",
            "RESIDENCIAL_NORTE",
            "SC401_CORREDOR",
            "SAO_JOSE_CENTRO",
            "SAO_JOSE_BARREIROS",
            "SAO_JOSE_KOBRASOL",
            "SAO_JOSE_ROCADO",
            "PALHOCA_CENTRO",
            "PALHOCA_PEDRA_BRANCA",
            "PALHOCA_BR101_SUL",
            "BIGUACU_BR101_NORTE",
            "VIA_EXPRESSA_CORREDOR",
            "SANTO_AMARO",
            "GOV_CELSO_RAMOS",
            "ANTONIO_CARLOS"
    );

    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public ClusterName {
        if (value == null) { throw new IllegalArgumentException("Cluster name cannot be null"); }
        if (value.isBlank()) { throw new IllegalArgumentException("Cluster name cannot be blank"); }
        String normalizedValue = normalize(value);
        if (!VALID_CLUSTERS.contains(normalizedValue)) {
            throw new IllegalArgumentException(String.format("Cluster '%s' is not valid. Valid values: %s", value, VALID_CLUSTERS));
        }
    }

    /**
     * Strips diacritics (ç→c, á→a, etc.) and uppercases so both
     * SAO_JOSE_ROÇADO and SAO_JOSE_ROCADO match the same entry.
     */
    private static String normalize(String input) {
        String decomposed = Normalizer.normalize(input.trim().toUpperCase(), Normalizer.Form.NFD);
        return DIACRITICS.matcher(decomposed).replaceAll("");
    }

    /**
     * Crea una instancia de ClusterName con el valor proporcionado.
     *
     * @param clusterName el nombre del cluster
     * @return nueva instancia
     */
    @JsonCreator
    public static ClusterName of(String clusterName) {
        return new ClusterName(clusterName);
    }

    /**
     * Obtiene el valor para serialización JSON.
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}