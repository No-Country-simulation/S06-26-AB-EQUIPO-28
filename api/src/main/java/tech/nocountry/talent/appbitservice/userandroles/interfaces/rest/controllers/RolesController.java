package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.role.GetAllRolesEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.RolesDocs;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.RoleResource;

import java.util.List;

/**
 * Controlador de consulta de roles (solo lectura).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("dev")
public class RolesController implements RolesDocs {
    private final GetAllRolesEndpoint getAllRolesEndpoint;

    /**
     * Obtiene todos los roles del sistema.
     *
     * @return lista de recursos de roles
     */
    @Override
    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        List<RoleResource> response = getAllRolesEndpoint.handle();
        return ResponseEntity.ok(response);
    }
}
