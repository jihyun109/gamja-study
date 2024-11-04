package gamja.gamja_pre.entity.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_otps")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthOtpEntity {
    @Id
    private String userName;
    private String code;
}
