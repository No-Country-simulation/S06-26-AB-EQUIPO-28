package tech.nocountry.talent.appbitservice.userandroles.domain.model.commands;

import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record SignUpCommand(String username, String password, boolean isActive, List<Role> roles, Optional<UUID> userId) {
    public static SignUpCommand create(String username, String password, boolean isActive, List<Role> roles) {
        return new SignUpCommand(username, password, isActive, roles, Optional.empty());
    }
}
