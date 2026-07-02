package tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects;

import java.util.List;
import java.util.UUID;

public record UserSummary(
        UUID userId,
        String username,
        List<String> roles,
        boolean isActive
) { }
