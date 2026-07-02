package tech.nocountry.talent.appbitservice.telemetry.domain.model.queries;

import org.springframework.lang.Nullable;

import java.time.LocalDate;

/**
 * Query para buscar concentraciones con filtros opcionales y paginación.
 *
 * <p>Permite buscar métricas de concentración de red filtrando por:</p>
 * <ul>
 *   <li>Cluster geográfico (a través de la entidad Antenna)</li>
 *   <li>Rango de fechas</li>
 *   <li>Período de sesión</li>
 *   <li>Porcentaje mínimo de drop</li>
 * </ul>
 *
 * <p>Incluye parámetros de paginación ({@code page}, {@code size}) para soportar
 * respuestas paginadas en los endpoints REST.</p>
 */
public record GetConcentrationFilteredQuery(
        @Nullable String cluster,
        @Nullable LocalDate startDate,
        @Nullable LocalDate endDate,
        @Nullable String period,
        @Nullable Double minDropPct,
        int page,
        int size
) {
    public GetConcentrationFilteredQuery {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("size must be >= 1");
    }
}
