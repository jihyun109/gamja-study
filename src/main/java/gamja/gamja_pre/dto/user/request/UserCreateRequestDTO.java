package gamja.gamja_pre.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserCreateRequestDTO {
    @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "이름은 영문과 숫자만 포함할 수 있습니다.")
    @Size(min = 2, max = 20, message = "이름 2자 이상 20자 이하이어야 합니다.")
    private String userName;

    @NotNull(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;
}