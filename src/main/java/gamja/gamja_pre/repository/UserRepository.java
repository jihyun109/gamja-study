package gamja.gamja_pre.repository;

import gamja.gamja_pre.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findAllByOrderByIdAsc(Pageable pageable);
    Slice<UserEntity> findSliceByOrderByIdAsc(Pageable pageable);
    Optional<UserEntity> findByEmail(String email);
}
