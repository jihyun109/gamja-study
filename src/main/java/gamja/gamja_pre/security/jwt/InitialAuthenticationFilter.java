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
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

// JWT 필터 : JWT 토큰을 확인하고, 인증된 사용자인지 확인하는 필터
// login 요청 -> 클라이언트로부터 받은 사용자명, 비밀번호, OTP를 검증 -> 인증되면 JWT 토큰을 생성, Authorization 헤더에 삽입해 응답을 반환
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter: 필터가 한 번만 실행되도록 보장

    private final AuthenticationManager manager;
    @Value("${jwt.signing.key}")    // 속성파일에서 JWT 토큰에 서명하는 데 이용한 키 값을 가져옴
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 요청 본문 읽기
        StringBuilder body = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

//        System.out.println(body);

        // JSON -> DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginRequestDTO loginRequestDTO = objectMapper.readValue(body.toString(), UserLoginRequestDTO.class);

        // DTO에서 값 추출
        String userName = loginRequestDTO.getUserName();
        String password = loginRequestDTO.getPassword();
        String code = loginRequestDTO.getCode();

        try {
            if (code == null) { // OTP가 없으면 UsernamePasswordAuthentication(사용자 이름과 암호)로 인증
                UsernamePasswordAuthentication a = new UsernamePasswordAuthentication(userName, password);
                manager.authenticate(a);
            } else {    // otp가 있는 경우 OtpAuthentication 를 사용해 인증 -> JWT 생성 -> 응답 헤더에 추가
                OtpAuthentication a = new OtpAuthentication(userName, password);
                manager.authenticate(a);    // 인증

                // 대칭 키로 서명 생성
                SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

                // jwt 생성
                String jwt = Jwts.builder()
                        .setClaims(Map.of("userName", userName))
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 만료 시간 설정 (1시간 후)
                        .signWith(key)
                        .compact();

                response.setHeader("Authorization", "Bearer " + jwt);
            }
        } catch (AuthenticationException ex) {  // 인증 실패 시
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid credentials");
        }
    }

    // /login 경로에만 이 이 필터를 적용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
