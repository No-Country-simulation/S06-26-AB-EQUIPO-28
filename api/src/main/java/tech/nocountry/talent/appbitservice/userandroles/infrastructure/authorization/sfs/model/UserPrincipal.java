package tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {
    private final UUID userId;
    private final String username;
    private final List<String> roles;
}
