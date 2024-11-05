package gamja.gamja_pre.config;

import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Bean 수동 등록
@Configuration
public class Config {
    @Bean
    public UserServiceImpl userServiceImpl(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    // 프락시 클래스에 대한 RestTemplate 빈 정의
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
