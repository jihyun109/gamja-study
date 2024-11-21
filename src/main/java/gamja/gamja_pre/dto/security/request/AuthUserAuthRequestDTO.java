package gamja.gamja_pre.dto.security.request;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AuthUserAuthRequestDTO {
    private String username;
    private String password;
}
