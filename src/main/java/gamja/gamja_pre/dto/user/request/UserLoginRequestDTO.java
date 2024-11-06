package gamja.gamja_pre.dto.user.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserLoginRequestDTO {
    private String userName;
    private String password;
    private String code;
}
