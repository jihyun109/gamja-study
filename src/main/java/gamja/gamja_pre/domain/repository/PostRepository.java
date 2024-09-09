package gamja.gamja_pre.domain.repository;

import gamja.gamja_pre.domain.entity.PostEntity;
import gamja.gamja_pre.dto.request.PostRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Slice<PostRequest> findSliceByOrderByCreatedAtAsc(Pageable pageable);

}
