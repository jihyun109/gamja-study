package gamja.gamja_pre.security.jwt;

import gamja.gamja_pre.dto.security.request.CheckOtpDTO;
import gamja.gamja_pre.security.OtpAuthentication;
import gamja.gamja_pre.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
@RequiredArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());


        CheckOtpDTO checkOtpDTO = new CheckOtpDTO(username, code);

        userService.check(checkOtpDTO); // AuthService에서 인증 처리
        return new OtpAuthentication(username, code);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
