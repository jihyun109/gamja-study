package gamja.gamja_pre.config;

import gamja.gamja_pre.security.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Value("${jwt.signing.key}")
    public String key;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(key);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {return new JwtAuthenticationFilter(key);}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup").permitAll()  // /login, /signup 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증된 사용자만 접근 가능
                )

                // 필터 순서 설정
                .addFilterAt(new InitialAuthenticationFilter(jwtTokenProvider(), usernamePasswordAuthenticationProvider), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);

        // JwtSecurityConfig를 적용하려면 직접 configure를 호출
        new JwtSecurityConfig().configure(http);

        return http.build();  // 필터 체인 빌드
    }
}


