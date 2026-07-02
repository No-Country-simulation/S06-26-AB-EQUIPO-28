package tech.nocountry.talent.appbitservice.demographics.domain.model.queries;

import jakarta.annotation.Nullable;

/**
 * Query para obtener ciudadanos filtrados por nivel de ingreso y/o grupo de edad, con paginación.
 *
 * <p>Unifica los anteriores queries individuales en uno solo que permite filtrar por
 * {@code incomeLevel} y {@code ageGroup} opcionales, siempre con paginación obligatoria.</p>
 *
 * @param incomeLevel nivel de ingreso (A, B, C, D) — opcional
 * @param ageGroup    grupo de edad (18-24, 25-34, 35-44, 45-54, 55+) — opcional
 * @param page        número de página (0-based)
 * @param size        tamaño de página (1-100)
 */
public record GetCitizensFilteredQuery(
        @Nullable String incomeLevel,
        @Nullable String ageGroup,
        int page,
        int size
) {
    public GetCitizensFilteredQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
    }

    /**
     * Crea una instancia por defecto: sin filtros, primera página, 20 elementos.
     *
     * @return query vacía con valores por defecto
     */
    public static GetCitizensFilteredQuery empty() {
        return new GetCitizensFilteredQuery(null, null, 0, 20);
    }
}
