package tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.model.UserPrincipal;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.tokens.jwt.BearerTokenService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {
    private final BearerTokenService tokenService;

    @Override
    @NullMarked
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {
        try {
            String token = tokenService.getBearerTokenFrom(request);
            if (token != null && tokenService.validateToken(token)) {
                String username = tokenService.getUsernameFromToken(token);
                List<String> roles = tokenService.getRolesFromToken(token);
                var userId = tokenService.getUserIdFromToken(token);

                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var principal = new UserPrincipal(userId, username, roles);
                var authToken = new UsernamePasswordAuthenticationToken(
                        principal, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Authentication failure is handled silently - request continues without authentication
        }
        filterChain.doFilter(request, response);
    }
}
