package gamja.gamja_pre.dto.security.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CheckOtpDTO {
    private String userName;
    private String code;
}
