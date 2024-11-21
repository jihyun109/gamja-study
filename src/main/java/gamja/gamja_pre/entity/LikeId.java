package gamja.gamja_pre.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LikeId implements Serializable {
    private Long sender;
    private Long post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(sender, likeId.sender) && Objects.equals(post, likeId.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, post);
    }
}
