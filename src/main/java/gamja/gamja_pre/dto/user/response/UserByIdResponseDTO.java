package gamja.gamja_pre.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserByIdResponseDTO {
    private Long id;
    private String userName;
    private String email;
}