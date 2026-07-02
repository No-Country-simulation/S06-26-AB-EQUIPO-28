package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource DTO raw del upstream Demographics BC.
 *
 * <p>Este record representa el modelo crude del bounded context demographics.
 * <strong>NO</strong> debe ser importado en el dominio de inclusion-core.
 * La traducción happens en el Assembler layer.</p>
 */
@Schema(description = "Resource raw de ciudadano del upstream Demographics BC")
public record DemographicsCitizenResource(
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