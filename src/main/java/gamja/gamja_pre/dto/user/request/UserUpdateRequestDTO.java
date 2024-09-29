package gamja.gamja_pre.dto.user.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserUpdateRequestDTO {
    private String userName;
    private String email;
}