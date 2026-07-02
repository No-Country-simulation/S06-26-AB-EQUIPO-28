package tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.services;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserName;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.authorization.sfs.model.UserDetailsImpl;
import tech.nocountry.talent.appbitservice.userandroles.infrastructure.persistence.jpa.repositories.UserRepository;

/** Servicio de carga de detalles de usuario para Spring Security. */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /** Carga los detalles del usuario desde la base de datos. */
    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(new UserName(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + username));
        return UserDetailsImpl.build(user);
    }
}

