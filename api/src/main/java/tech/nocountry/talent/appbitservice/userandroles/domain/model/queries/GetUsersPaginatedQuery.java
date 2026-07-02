package tech.nocountry.talent.appbitservice.userandroles.domain.model.queries;

public record GetUsersPaginatedQuery(
        int page,
        int size,
        String search
) { }