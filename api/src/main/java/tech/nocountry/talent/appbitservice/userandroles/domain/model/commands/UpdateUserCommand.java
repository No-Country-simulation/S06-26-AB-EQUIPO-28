package tech.nocountry.talent.appbitservice.userandroles.domain.model.commands;

import java.util.List;
import java.util.UUID;

public record UpdateUserCommand (
    UUID userId, 
    String username, 
    String password, 
    Boolean isActive,
    List<String> roles
) { }
