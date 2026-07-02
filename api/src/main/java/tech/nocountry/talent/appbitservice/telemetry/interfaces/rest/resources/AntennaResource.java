package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources.AntennaResourceDocs;

/**
 * Resource DTO for Antenna entity in REST responses.
 *
 * <p>Represents the antenna data exposed through the API.
 */
@Schema(description = AntennaResourceDocs.DESCRIPTION)
public record AntennaResource(
        @Schema(description = AntennaResourceDocs.ECGI, example = "530011001234")
        String ecgi,

        @Schema(description = AntennaResourceDocs.CLUSTER, example = "Norte")
        String cluster,

        @Schema(description = AntennaResourceDocs.MUNICIPALITY, example = "Florianópolis")
        String municipality,

        @Schema(description = AntennaResourceDocs.LATITUDE, example = "-27.5917")
        Double latitude,

        @Schema(description = AntennaResourceDocs.LONGITUDE, example = "-48.5588")
        Double longitude
) { }