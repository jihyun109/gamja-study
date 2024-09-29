package gamja.gamja_pre.domain.repository;

import gamja.gamja_pre.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findPagedAllUsers(Pageable pageable);
    Slice<UserEntity> findSliceAllUsers(Pageable pageable);
}
