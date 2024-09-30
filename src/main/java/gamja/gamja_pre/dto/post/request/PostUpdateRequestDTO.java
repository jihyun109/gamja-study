package gamja.gamja_pre.dto.post.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDTO {
    private Long id;
    @NotNull(message = "Title is required.")
    private String title;
    @NotNull(message = "Content is required")
    private String content;
    private LocalDateTime createdAt;
}
