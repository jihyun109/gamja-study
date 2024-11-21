package gamja.gamja_pre.dto.security.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
