package gamja.gamja_pre.repository.security;

import gamja.gamja_pre.entity.security.AuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUserEntity, String> { // ?
    Optional<AuthUserEntity> findAuthUserEntityByUserName(String userName);
}
