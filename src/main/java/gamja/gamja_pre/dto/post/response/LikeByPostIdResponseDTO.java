package gamja.gamja_pre.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeByPostIdResponseDTO {
    private long senderId;
    private String senderName;
}
