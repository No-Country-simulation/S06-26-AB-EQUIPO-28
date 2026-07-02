package tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user.DeleteUserEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user.GetAllUsersEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user.GetUserByIdEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.internal.user.UpdateUserEndpoint;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.docs.UsersDocs;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UpdateUserResource;
import tech.nocountry.talent.appbitservice.userandroles.interfaces.rest.resources.UserResource;

import java.util.UUID;

/**
 * Controlador de usuarios (comandos) para entorno de desarrollo.
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("dev")
public class UsersController implements UsersDocs {
    private final UpdateUserEndpoint updateUserEndpoint;
    private final DeleteUserEndpoint deleteUserEndpoint;
    private final GetAllUsersEndpoint getAllUsersEndpoint;
    private final GetUserByIdEndpoint getUserByIdEndpoint;

    /**
     * Actualiza los datos de un usuario.
     *
     * @param userId el ID del usuario a actualizar
     * @param userResource el recurso con los datos de actualización
     * @return el recurso con los datos actualizados del usuario
     */
    @Override
    @PatchMapping(value = "/{userId}")
    public ResponseEntity<UserResource> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserResource userResource
    ) {
        return ResponseEntity.ok(updateUserEndpoint.handle(userId, userResource));
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param userId el ID del usuario a eliminar
     * @return respuesta sin contenido (204)
     */
    @Override
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        deleteUserEndpoint.handle(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los usuarios del sistema, opcionalmente paginados.
     *
     * @param page número de página (opcional)
     * @param size tamaño de página (opcional)
     * @param search término de búsqueda (opcional)
     * @return lista de usuarios o respuesta paginada
     */
    @Override
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search
    ) {
        if (page != null && size != null) {
            Page<UserResource> response = getAllUsersEndpoint.handlePaginated(page, size, search);
            return ResponseEntity.ok(response);
        }

        var listResponse = getAllUsersEndpoint.handle();
        return ResponseEntity.ok(listResponse);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId el ID del usuario
     * @return el recurso del usuario encontrado
     */
    @Override
    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(getUserByIdEndpoint.handle(userId));
    }
}