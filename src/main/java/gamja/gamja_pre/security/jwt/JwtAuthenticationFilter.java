package gamja.gamja_pre.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
// 토큰 검증, 인증 정보를 자동으로 설정(사용자 인증), 인증된 사용자 정보를 SecurityContext 에 설정
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Key key;

    public JwtAuthenticationFilter(@Value("${jwt.signing.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token missing or invalid");
            return;
        }

        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

        System.out.println("key:" + key);

        // 'Bearer ' 접두어를 제거하고 실제 JWT 토큰만 가져오기
        jwt = jwt.substring(7);

        // 토큰에서 클레임을 얻고 서명 검증.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        System.out.println("claims:" + claims);

        String userName = String.valueOf(claims.get("userName"));

        // SecurityContext 에 추가할 Authentication 인스턴스 생성
        GrantedAuthority a = new SimpleGrantedAuthority("user");
        var auth = new UsernamePasswordAuthentication(
                userName,
                null,
                List.of(a));

        // SecurityContext에 Authentication 객체 추가
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        filterChain.doFilter(request, response);    // 필터 체인의 다음 필터 호룿
    }

    // /login 요청에는 트리거되지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/login")|| request.getServletPath().equals("/signup") || request.getServletPath().equals("/users/paged") ;
    }
}
