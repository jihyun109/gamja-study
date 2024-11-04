package gamja.gamja_pre.repository;

import gamja.gamja_pre.entity.LikeEntity;
import gamja.gamja_pre.domain.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {
    List<LikeEntity> findUserIdsByPostId(Long postId);

    boolean existsById(LikeId likeId);

    void deleteById(LikeId likeId);
}
