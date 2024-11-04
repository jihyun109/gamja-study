package gamja.gamja_pre.security.service;

import gamja.gamja_pre.dto.security.request.AuthUserAuthRequestDTO;
import gamja.gamja_pre.dto.security.request.AuthUserCreateRequestDTO;
import gamja.gamja_pre.dto.security.request.CheckOtpDTO;
import gamja.gamja_pre.entity.security.AuthUserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface AuthUserService {
    void addUser(AuthUserCreateRequestDTO user);
    void auth(AuthUserAuthRequestDTO userAuthRequestDTO);
    boolean check(CheckOtpDTO checkOtpDTO);
}
