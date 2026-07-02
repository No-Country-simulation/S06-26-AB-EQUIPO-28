package tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;
import java.util.UUID;

/** Constructor de UsernamePasswordAuthenticationToken. */
public class UsernamePasswordAuthenticationTokenBuilder {
    private UsernamePasswordAuthenticationTokenBuilder () { }

    public static UsernamePasswordAuthenticationToken build(UserDetails principal, HttpServletRequest request) {
        UUID userId = null;
        List<String> roles = List.of();
        if (principal instanceof UserDetailsImpl userDetails) {
            userId = userDetails.getUserId();
            roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }
        var userPrincipal = new UserPrincipal(userId, principal.getUsername(), roles);
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, principal.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }
}

