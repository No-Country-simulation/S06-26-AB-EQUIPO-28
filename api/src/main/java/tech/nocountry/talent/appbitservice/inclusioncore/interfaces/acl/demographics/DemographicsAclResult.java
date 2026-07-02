package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resultado ACL para datos demográficos.
 *
 * <p>Este record representa el modelo limpio para el bounded context inclusion-core,
 * aislado del modelo original del bounded context demographics.Translation
 * happens in the Assembler layer.</p>
 */
@Schema(description = "Resultado de perfil demográfico para análisis de inclusión social")
public record DemographicsAclResult(
        @Schema(description = "Hash anonimizado del ciudadano", example = "a1b2c3d4e5f6")
        String citizenHash,

        @Schema(description = "Nivel de ingreso (A, B, C, D)", example = "D")
        String incomeLevel,

        @Schema(description = "Grupo de edad", example = "18-24")
        String ageGroup,

        @Schema(description = "Patrón de movilidad", example = "LOW")
        String mobilityPattern,

        @Schema(description = "Cluster geográfico de residencia", example = "Norte")
        String homeCluster
) { }