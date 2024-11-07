package gamja.gamja_pre.config;

import gamja.gamja_pre.security.jwt.InitialAuthenticationFilter;
import gamja.gamja_pre.security.jwt.JwtAuthenticationFilter;
import gamja.gamja_pre.security.jwt.OtpAuthenticationProvider;
import gamja.gamja_pre.security.jwt.UsernamePasswordAuthenticationProvider;
import gamja.gamja_pre.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public InitialAuthenticationFilter initialAuthenticationFilter(AuthenticationManager authManager) {
        return new InitialAuthenticationFilter(authManager);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public OtpAuthenticationProvider otpAuthenticationProvider() {
        return new OtpAuthenticationProvider(userService);
    }

    @Bean
    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
        return new UsernamePasswordAuthenticationProvider(userService);
    }

    // AuthenticationProvider를 관리
    @Bean
    public AuthenticationManager authManager() {
        // AuthenticationProvider 목록 설정
        List<AuthenticationProvider> providers = Arrays.asList(
                otpAuthenticationProvider(),
                usernamePasswordAuthenticationProvider()
        );
        return new ProviderManager(providers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/users/signup").permitAll()  // /login, /signup 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증된 사용자만 접근 가능
                )

                // 필터 순서 설정
                .addFilterAt(initialAuthenticationFilter(authManager), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        return http.build();  // 필터 체인 빌드
    }

}
