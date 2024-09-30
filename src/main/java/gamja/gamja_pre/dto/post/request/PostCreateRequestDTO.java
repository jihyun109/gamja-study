package gamja.gamja_pre.dto.post.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostCreateRequestDTO {
    private Long id;
    @NotNull(message = "Title is required.")
    private String title;
    @NotNull(message = "Content is required")
    private String content;
    private LocalDateTime createdAt;
}
