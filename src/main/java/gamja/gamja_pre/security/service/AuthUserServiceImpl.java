package gamja.gamja_pre.security.service;

import gamja.gamja_pre.dto.security.request.AuthUserAuthRequestDTO;
import gamja.gamja_pre.dto.security.request.AuthUserCreateRequestDTO;
import gamja.gamja_pre.dto.security.request.CheckOtpDTO;
import gamja.gamja_pre.entity.security.AuthOtpEntity;
import gamja.gamja_pre.entity.security.AuthUserEntity;
import gamja.gamja_pre.repository.security.AuthOtpRepository;
import gamja.gamja_pre.repository.security.AuthUserRepository;
import gamja.gamja_pre.security.util.GenerateCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthOtpRepository authOtpRepository;

    @Override
    public void addUser(AuthUserCreateRequestDTO userDTO) {
        AuthUserEntity authUserEntity = AuthUserEntity.builder()
                .userName(userDTO.getUserName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
        authUserRepository.save(authUserEntity);
    }

    @Override
    public void auth(AuthUserAuthRequestDTO userAuthRequestDTO) {
        Optional<AuthUserEntity> optionalAuthUser = // db에서 사용자 검색
                authUserRepository.findAuthUserEntityByUserName(userAuthRequestDTO.getUserName());

        if (optionalAuthUser.isPresent()) {  // 사용자가 있으면 암호 확인
            AuthUserEntity user = optionalAuthUser.get();
            if (passwordEncoder.matches(userAuthRequestDTO.getPassword(), user.getPassword())) {    // 암호가 맞으면 새 OTP 생성
                renewOtp(user);
            } else {
                throw new BadCredentialsException("Bad credentials. Invalid username or password.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials. User not found.");
        }
    }

    // 사용자의 OTP 검증 메서드
    @Override
    public boolean check(CheckOtpDTO checkOtpDTO) {
        Optional<AuthOtpEntity> userOtp = authOtpRepository.findAuthOtpEntityByUserName(checkOtpDTO.getUserName()); // 사용자 OTP 검색

        // db에 OTP 가 있고 받은 OTP와 일치하면 true 반환
        if (userOtp.isPresent()) {
            AuthOtpEntity otp = userOtp.get();
            if (checkOtpDTO.getCode().equals(otp.getCode())) {
                return true;
            }
        }
        return false;
    }

    private void renewOtp(AuthUserEntity user) {
        String code = GenerateCodeUtil.generateCode();  // OTP를 위한 임의의 수 생성
        Optional<AuthOtpEntity> userOtp = authOtpRepository.findAuthOtpEntityByUserName(user.getUserName());    // 사용자 이름으로 OTP 검색

        if (userOtp.isPresent()) {
            AuthOtpEntity otp = userOtp.get();
            AuthOtpEntity updatedOtp = AuthOtpEntity.builder()
                    .userName(otp.getUserName())  // 기존 사용자 이름 유지
                    .code(code)  // 새로운 OTP 코드 설정
                    .build();
            authOtpRepository.save(updatedOtp); // 업데이트된 OTP 저장
        } else {
            AuthOtpEntity newOtp = AuthOtpEntity.builder()
                    .userName(user.getUserName())
                    .code(code)
                    .build();
            authOtpRepository.save(newOtp);
        }
    }

}
