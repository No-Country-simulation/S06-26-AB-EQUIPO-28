package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources.CitizenDocs;

/**
 * Resource DTO for updating an existing citizen profile.
 *
 * <p>All fields are optional to support partial updates.
 * Only non-null fields are applied to the existing profile.</p>
 */
@Schema(description = CitizenDocs.UPDATE_CITIZEN_RESOURCE)
public record UpdateCitizenResource(
        /** Nivel de ingreso del ciudadano (A, B, C, D). Proviene del campo income_level del CSV de suscriptores. */
        @Schema(description = CitizenDocs.INCOME_LEVEL, example = "D")
        String incomeLevel,

        @Schema(description = CitizenDocs.AGE_GROUP, example = "18-24")
        String ageGroup,

        @Schema(description = CitizenDocs.MOBILITY_PATTERN, example = "LOW")
        String mobilityPattern,

        @Schema(description = CitizenDocs.HOME_CLUSTER, example = "Norte")
        String homeCluster
) { }
