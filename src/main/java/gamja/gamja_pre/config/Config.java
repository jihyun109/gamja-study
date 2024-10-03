package gamja.gamja_pre.config;

import gamja.gamja_pre.domain.repository.UserRepository;
import gamja.gamja_pre.domain.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Bean 수동 등록
@Configuration
public class Config {
    @Bean
    public UserServiceImpl userServiceImpl(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}
