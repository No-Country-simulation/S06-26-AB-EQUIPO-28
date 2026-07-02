package tech.nocountry.talent.appbitservice.userandroles.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.outboundservices.tokens.TokenService;

/** Interfaz de servicio de tokens Bearer JWT. */
public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest token);
}
