package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.telemetry.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource DTO raw del upstream Telemetry BC.
 *
 * <p>Este record representa el modelo crude del bounded context telemetry.
 * <strong>NO</strong> debe ser importado en el dominio de inclusion-core.
 * La traducción happens en el Assembler layer.</p>
 */
@Schema(description = "Resource raw de concentration del upstream Telemetry BC")
public record TelemetryConcentrationResource(
        @Schema(description = "Cluster geográfico", example = "Norte")
        String cluster,

        @Schema(description = "Porcentaje de paquetes descartados (drop)", example = "3.5")
        Double dropPct,

        @Schema(description = "Cantidad de antenas en el cluster", example = "45")
        Integer antennaCount,

        @Schema(description = "Latitud promedio del cluster", example = "-27.5917")
        Double averageLatitude,

        @Schema(description = "Longitud promedio del cluster", example = "-48.5588")
        Double averageLongitude
) { }