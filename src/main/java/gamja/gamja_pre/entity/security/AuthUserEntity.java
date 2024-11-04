package gamja.gamja_pre.entity.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "auth_users")
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserEntity {
    @Id
    private String userName;
    private String password;

}
