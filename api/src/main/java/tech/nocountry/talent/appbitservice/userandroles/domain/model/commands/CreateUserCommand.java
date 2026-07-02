package tech.nocountry.talent.appbitservice.userandroles.domain.model.commands;

import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;

public record CreateUserCommand(String username, String password, boolean isActive, Role role) { }