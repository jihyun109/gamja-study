package gamja.gamja_pre.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

// Bean 수동 등록
@Configuration
@EnableMethodSecurity(prePostEnabled = true)    // 전역 메서드 보안 활성화
public class Config {

}
