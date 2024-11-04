package gamja.gamja_pre.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
// Lombok 어노테이션
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성을 DB에 위임
    private Long id;
    private String title;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;    // 생성 시각 자동 기록. 생성된 날짜와 시간이 자동적으로 저장됨.

    @ManyToOne
    @JoinColumn(name="WRITER_ID")
    private UserEntity userEntity;

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
