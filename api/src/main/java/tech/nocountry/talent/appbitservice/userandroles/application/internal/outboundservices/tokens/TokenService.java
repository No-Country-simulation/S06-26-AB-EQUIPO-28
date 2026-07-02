package tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.tokens;

import java.util.List;
import java.util.UUID;

/** Interfaz del servicio de generación y validación de tokens. */
public interface TokenService {

    /** Genera un token para un usuario con sus roles y userId. */
    String generateToken(String identifier, List<String> roles, UUID userId);

    /** Extrae el nombre de usuario de un token. */
    String getUsernameFromToken(String token);

    /** Extrae los roles de un token. */
    List<String> getRolesFromToken(String token);

    /** Extrae el userId de un token. */
    UUID getUserIdFromToken(String token);

    /** Valida un token. */
    boolean validateToken(String token);
}

