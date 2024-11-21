package gamja.gamja_pre.security.jwt;

import gamja.gamja_pre.dto.security.request.AuthUserAuthRequestDTO;
import gamja.gamja_pre.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authenticationToken) throws AuthenticationException {
        String username = String.valueOf(authenticationToken.getPrincipal());
        String password = String.valueOf(authenticationToken.getCredentials());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // db에서 찾은 user 정보

        userService.auth(new AuthUserAuthRequestDTO(username, password), userDetails);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
