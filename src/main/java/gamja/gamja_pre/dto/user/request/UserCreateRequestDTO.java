package gamja.gamja_pre.dto.user.request;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserCreateRequestDTO {
    private String userName;
    private String email;
}