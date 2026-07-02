package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources.CitizenDocs;

/**
 * Resource DTO for CitizenProfile entity in REST responses.
 *
 * <p>Represents the demographic profile of a citizen exposed through the API.
 */
@Schema(description = CitizenDocs.RECORD_DESCRIPTION)
public record CitizenResource(
        @Schema(description = CitizenDocs.CITIZEN_HASH, example = "a1b2c3d4e5f6")
        String citizenHash,

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