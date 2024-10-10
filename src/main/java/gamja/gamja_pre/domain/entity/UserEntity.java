package gamja.gamja_pre.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
// Lombok 어노테이션
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성을 DB에 위임
    private Long id;
    private String userName;
    private String email;

    //
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> posts;

    public void updateUser(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
}
