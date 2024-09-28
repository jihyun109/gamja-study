package gamja.gamja_pre.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
