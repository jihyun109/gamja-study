package gamja.gamja_pre.domain.repository;

import gamja.gamja_pre.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // 페이지네이션된 결과를 빠르게 반환하고 추가적인 데이터 변환이 필요하지 않으면 Entity 씀
    // Entity 는 데이터 베이스 모델과 직접적으로 연결되어 있어 데이터베이스의 상태를 직접적으로 반영함.
    Slice<PostEntity> findSliceByOrderByCreatedAtAsc(Pageable pageable);
    Page<PostEntity> findAllByOrderByCreatedAtAsc(Pageable pageable);
    List<PostEntity> findByTitleContains(String keyword);   // 제목에 keyword가 포함되어있는 게시물 조회
}