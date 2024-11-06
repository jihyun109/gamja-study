package gamja.gamja_pre.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamja.gamja_pre.dto.user.request.UserLoginRequestDTO;
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
import java.io.BufferedReader;
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
        // 요청 본문을 String으로 읽음
        StringBuilder body = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        // JSON -> DTO 변환 (예: ObjectMapper를 사용)
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginRequestDTO loginRequestDTO = objectMapper.readValue(body.toString(), UserLoginRequestDTO.class);

        // DTO에서 값 추출
        String userName = loginRequestDTO.getUserName();
        String password = loginRequestDTO.getPassword();
        String code = loginRequestDTO.getCode();

        if (code == null) { // OTP가 없으면 사용자 이름과 암호로 인증
            UsernamePasswordAuthentication a = new UsernamePasswordAuthentication(userName, password);
            manager.authenticate(a);
        } else {
            OtpAuthentication a = new OtpAuthentication(userName, password);
            a = (OtpAuthentication) manager.authenticate(a);

            // 대칭 키로 서명 생성
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

            String jwt = Jwts.builder()
                    .setClaims(Map.of("userName", userName))
                    .signWith(key)
                    .compact();

            response.setHeader("Authorization", "Bearer " + jwt);
        }
    }

    // /login 경로에만 이 이 필터를 적용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
