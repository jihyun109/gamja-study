package gamja.gamja_pre.controller.security;

import gamja.gamja_pre.dto.security.request.AuthUserAuthRequestDTO;
import gamja.gamja_pre.dto.security.request.AuthUserCreateRequestDTO;
import gamja.gamja_pre.dto.security.request.CheckOtpDTO;
import gamja.gamja_pre.entity.security.AuthUserEntity;
import gamja.gamja_pre.security.service.AuthUserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthUserServiceImpl authUserService;

    @PostMapping("/auth-users/add")
    public void addAuthUser(@RequestBody AuthUserCreateRequestDTO userCreateRequestDTO) {
        authUserService.addUser(userCreateRequestDTO);
    }

    @PostMapping("/auth-users/auth")
    public void auth(@RequestBody AuthUserAuthRequestDTO userAuthRequestDTO) {
        authUserService.auth(userAuthRequestDTO);
    }

    @PostMapping("auth-otps/check")
    public void check(@RequestBody CheckOtpDTO checkOtpDTO, HttpServletResponse response) {
        // OTP 가 유요하면 HTTP 응답 코드 200 반환, 아니면 403 반환
        if (authUserService.check(checkOtpDTO)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
