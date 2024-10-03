package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.domain.entity.PostEntity;
import gamja.gamja_pre.domain.repository.PostRepository;
import gamja.gamja_pre.dto.post.request.*;
import gamja.gamja_pre.dto.post.response.*;
import gamja.gamja_pre.error.ErrorCode;
import gamja.gamja_pre.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional  // 클래스 레벨이 적용해 클래스 내의 모든 메서드가 동일한 트랜잭션 관리하에 동작하도록 함.
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    // Page : 전체 페이지 정보와 메타 데이터 제공. 총 페이지 수, 전체 데이터 수 등의 정보
    // Slice : 현재 페이지에 대한 데이터만 제공. 전체 페이지 수에 대한 정보는 포함되지 않음

    @Override
    @Transactional(readOnly = true)
    public Page<PostPagedListResponseDTO> getPagedPosts(int pageNumber, int pageSize) {   // 페이징 처리
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostEntity> postEntityPage = postRepository.findAllByOrderByCreatedAtAsc(pageable);

        List<PostPagedListResponseDTO> postRequests = mapToResponseDTO(postEntityPage.getContent(), this::convertToPostPagedListResponseDTO);

        return new PageImpl<>(postRequests, pageable, postEntityPage.getTotalElements());
    }

//    // List<PostEntity>를 List<PostPagedListResponseDTO> 로 변환
//    private List<PostPagedListResponseDTO> mapToPostPagedListResponseDTO(List<PostEntity> postEntities) {
//        return postEntities.stream()
//                .map(this::convertToPostPagedListResponseDTO)
//                .collect(Collectors.toList());
//    }

    // Entity -> PostPagedListResponseDTO 변환
    private PostPagedListResponseDTO convertToPostPagedListResponseDTO(PostEntity postEntity) {
        return new PostPagedListResponseDTO(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PostScrollListResponseDTO> getInfiniteScrollPosts(int pageNumber, int pageSize) {   // 무한 스크롤
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<PostEntity> postEntitySlice = postRepository.findSliceByOrderByCreatedAtAsc(pageable);

        List<PostScrollListResponseDTO> postRequests = mapToResponseDTO(postEntitySlice.getContent(), this::convertToPostScrollListResponseDTO);

        return new SliceImpl<>(postRequests, pageable, postEntitySlice.hasNext());
    }

//    // List<PostEntity>를 List<PostScrollListResponseDTO> 로 변환
//    private List<PostScrollListResponseDTO> mapToPostScrollListResponse(List<PostEntity> postEntities) {
//        return postEntities.stream()
//                .map(this::convertToPostScrollListResponseDTO)
//                .collect(Collectors.toList());
//    }

    // Entity -> PostScrollListResponseDTO 변환
    private PostScrollListResponseDTO convertToPostScrollListResponseDTO(PostEntity postEntity) {
        return new PostScrollListResponseDTO(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long id) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found with id: " + id, ErrorCode.NOT_FOUND));

        return new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSearchResponseDTO> getSearchByKeyword(String keyword) {
        List<PostEntity> posts = postRepository.findByTitleContains(keyword);
        List<PostSearchResponseDTO> postRequests = mapToResponseDTO(posts, this::convertToPostSearchResponseDTO);

        return postRequests;
    }

//    // List<PostEntity>를 List<PostSearchResponseDTO>로 변환
//    private List<PostSearchResponseDTO> mapToPostSearchResponse(List<PostEntity> postEntities) {
//        return postEntities.stream()
//                .map(this::convertToPostSearchResponseDTO)
//                .collect(Collectors.toList());
//    }

    // Entity -> PostSearchResponseDTO 변환 메서드
    private PostSearchResponseDTO convertToPostSearchResponseDTO(PostEntity postEntity) {
        return new PostSearchResponseDTO(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getCreatedAt()
        );
    }

    private <T> List<T> mapToResponseDTO(List<PostEntity> postEntities, Function<PostEntity, T> mapper) {
        return postEntities.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }


    @Override
    public void createPost(PostCreateRequestDTO postCreateRequest) {
        PostEntity post = PostEntity.builder()
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .build();
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long id, PostUpdateRequestDTO postUpdateRequestDTO) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id, ErrorCode.NOT_FOUND));
        post.updatePost(postUpdateRequestDTO.getTitle(), postUpdateRequestDTO.getContent());

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) throws NotFoundException {
        postRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id, ErrorCode.NOT_FOUND));
        postRepository.deleteById(id);
    }
}
