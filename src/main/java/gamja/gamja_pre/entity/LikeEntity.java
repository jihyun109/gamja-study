package gamja.gamja_pre.entity;

import gamja.gamja_pre.domain.id.LikeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@IdClass(LikeId.class)
public class LikeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private UserEntity sender;

    @Id
    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostEntity post;
}
