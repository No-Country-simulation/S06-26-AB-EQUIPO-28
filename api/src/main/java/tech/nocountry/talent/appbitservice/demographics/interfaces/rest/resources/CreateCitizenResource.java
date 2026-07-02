package tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.docs.resources.CitizenDocs;

/**
 * Resource DTO for creating a new citizen profile.
 *
 * <p>Accepts raw string values that are converted to domain Value Objects
 * by the command assembler.</p>
 */
@Schema(description = CitizenDocs.CREATE_CITIZEN_RESOURCE)
public record CreateCitizenResource(
        @Schema(description = CitizenDocs.CITIZEN_HASH, example = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6")
        @NotBlank(message = "El hash del ciudadano es obligatorio")
        @Size(min = 32, max = 64, message = "El hash debe tener entre 32 y 64 caracteres")
        String citizenHash,

        /** Nivel de ingreso del ciudadano (A, B, C, D). Proviene del campo income_level del CSV de suscriptores. */
        @Schema(description = CitizenDocs.INCOME_LEVEL, example = "D")
        @NotBlank(message = "El nivel de ingreso es obligatorio")
        String incomeLevel,

        @Schema(description = CitizenDocs.AGE_GROUP, example = "18-24")
        @NotBlank(message = "El grupo de edad es obligatorio")
        String ageGroup,

        @Schema(description = CitizenDocs.MOBILITY_PATTERN, example = "LOW")
        @NotBlank(message = "El patrón de movilidad es obligatorio")
        String mobilityPattern,

        @Schema(description = CitizenDocs.HOME_CLUSTER, example = "Norte")
        @NotBlank(message = "El cluster de residencia es obligatorio")
        String homeCluster
) { }
