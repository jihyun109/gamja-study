package gamja.gamja_pre.security.jwt;

import gamja.gamja_pre.security.OtpAuthentication;
import gamja.gamja_pre.security.UsernamePasswordAuthentication;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager manager;
    @Value("${jwt.signing.key}")    // 속성파일에서 JWT 토큰에 서명하는 데 이용한 키 값을 가져옴
    private String signingKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userName = request.getHeader("userName");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if (code == null) { // 요청에 OTP가 없으면 사용자 이름과 암호로 인증
            UsernamePasswordAuthentication a = new UsernamePasswordAuthentication(userName, password);
            manager.authenticate(a);
        } else {
            OtpAuthentication a = new OtpAuthentication(userName, password);
            a = (OtpAuthentication) manager.authenticate(a);

            // 대칭 키로 서명 생성
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

            String jwt = Jwts.builder() // JWT 구축, 사용자의 사용자 이름을 클레임 중 하나로 저장. 토큰을 서명하는 데 키를 이용.
                    .setClaims(Map.of("userName", userName))
                    .signWith(key)
                    .compact();

            // 토큰을 HTTP 응답의 권한 부여 헤더에 추가
            response.setHeader("Authorizatioin", jwt);;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
