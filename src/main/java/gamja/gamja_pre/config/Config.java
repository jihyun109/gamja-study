package gamja.gamja_pre.config;

import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.repository.security.AuthOtpRepository;
import gamja.gamja_pre.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

// Bean 수동 등록
@Configuration
@EnableMethodSecurity(prePostEnabled = true)    // 전역 메서드 보안 활성화
public class Config {
    @Bean
    public UserServiceImpl userServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthOtpRepository authOtpRepository) {
        return new UserServiceImpl(userRepository, passwordEncoder, authOtpRepository);
    }

    // 프락시 클래스에 대한 RestTemplate 빈 정의
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
