package gamja.gamja_pre.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// JWT 필터 : JWT 토큰을 확인하고, 인증된 사용자인지 확인하는 필터
// login 요청 -> 클라이언트로부터 받은 사용자명, 비밀번호, OTP를 검증 -> 인증되면 JWT 토큰을 생성, Authorization 헤더에 삽입해 응답을 반환
@Slf4j
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends UsernamePasswordAuthenticationFilter { // OncePerRequestFilter: 필터가 한 번만 실행되도록 보장

//    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
    @Value("${jwt.signing.key}")    // 속성파일에서 JWT 토큰에 서명하는 데 이용한 키 값을 가져옴

    // Login ID/PW 를 기반으로 AuthenticationToken 생성
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.warn("hihi");
        // 요청 본문 읽기
        StringBuilder body = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // JSON 요청 본문에서 username과 password 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestMap;
        try {
            requestMap = objectMapper.readValue(body.toString(), Map.class);    // 요청 본문을 Map으로 변환
        } catch (IOException e) {
            throw new RuntimeException("JSON 파싱 오류", e);
        }

        String username = requestMap.get("userName");
        String password = requestMap.get("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

        Authentication authentication = usernamePasswordAuthenticationProvider.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        return authentication;
    }

    // 사용자 인증이 성공했을 때 호출됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.warn("hihi2");


    }

    //    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // 요청 본문 읽기
//        StringBuilder body = new StringBuilder();
//        try (InputStream inputStream = request.getInputStream();
//             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                body.append(line);
//            }
//        }
//
//        // JSON -> DTO 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserLoginRequestDTO loginRequestDTO = objectMapper.readValue(body.toString(), UserLoginRequestDTO.class);
//
//        // DTO에서 값 추출
//        String userName = loginRequestDTO.getUserName();
//        String password = loginRequestDTO.getPassword();
//        String code = loginRequestDTO.getCode();
//
//        System.out.println(password);
//        try {
//            if (code == null) { // OTP가 없으면 UsernamePasswordAuthentication(사용자 이름과 암호)로 인증
//                UsernamePasswordAuthentication a = new UsernamePasswordAuthentication(userName, password);
//                manager.authenticate(a);
//            } else {    // otp가 있는 경우 OtpAuthentication 를 사용해 인증 -> JWT 생성 -> 응답 헤더에 추가
//                OtpAuthentication a = new OtpAuthentication(userName, password);
//                manager.authenticate(a);    // 인증
//
//                // 대칭 키로 서명 생성
//                SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
//
//                // jwt 생성
//                String jwt = Jwts.builder()
//                        .setClaims(Map.of("userName", userName))
//                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 만료 시간 설정 (1시간 후)
//                        .signWith(key)
//                        .compact();
//
//                response.setHeader("Authorization", "Bearer " + jwt);
//            }
//        } catch (AuthenticationException ex) {  // 인증 실패 시
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid credentials");
//        }
//    }
//
//    // /login 경로에만 이 이 필터를 적용
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return !request.getServletPath().equals("/login");
//    }
}
