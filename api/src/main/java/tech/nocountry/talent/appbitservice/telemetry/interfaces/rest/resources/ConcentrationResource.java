package tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.SessionPeriod;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.docs.resources.ConcentrationResourceDocs;

import java.time.LocalDate;

/**
 * Resource DTO for NetworkConcentration entity in REST responses.
 *
 * <p>Representa las métricas de concentración de red expuestas a través de la API.</p>
 *
 * <p>Anteriormente conocido como ConcentracionResource.</p>
 */
@Schema(description = ConcentrationResourceDocs.DESCRIPTION)
public record ConcentrationResource(
        @Schema(description = ConcentrationResourceDocs.ECGI, example = "530011001234")
        String ecgi,

        @Schema(description = ConcentrationResourceDocs.DAY_DATE, example = "2024-03-15")
        LocalDate dayDate,

        @Schema(description = ConcentrationResourceDocs.PERIOD, example = "MORNING")
        SessionPeriod period,

        @Schema(description = ConcentrationResourceDocs.USER_COUNT)
        Integer userCount,

        @Schema(description = ConcentrationResourceDocs.SESSION_COUNT)
        Integer sessionCount,

        @Schema(description = ConcentrationResourceDocs.DOWNLOAD_BYTES)
        Long downloadBytes,

        @Schema(description = ConcentrationResourceDocs.UPLOAD_BYTES)
        Long uploadBytes,

        @Schema(description = ConcentrationResourceDocs.AVERAGE_DURATION_S)
        Double averageDurationS,

        @Schema(description = ConcentrationResourceDocs.DROP_PCT)
        Double dropPct,

        @Schema(description = ConcentrationResourceDocs.CONGESTION_LEVEL)
        Double congestionLevel,

        @Schema(description = ConcentrationResourceDocs.TOTAL_CALLS)
        Long totalCalls,

        @Schema(description = ConcentrationResourceDocs.TOTAL_MESSAGES)
        Long totalMessages,

        @Schema(description = ConcentrationResourceDocs.LATITUDE, example = "-27.5917")
        Double latitude,

        @Schema(description = ConcentrationResourceDocs.LONGITUDE, example = "-48.5588")
        Double longitude
) { }