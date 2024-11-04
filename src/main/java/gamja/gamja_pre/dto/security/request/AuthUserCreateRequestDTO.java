package gamja.gamja_pre.dto.security.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthUserCreateRequestDTO {
    private String userName;
    private String password;
}
