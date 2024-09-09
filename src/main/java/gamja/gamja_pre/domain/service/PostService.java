package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.domain.entity.PostEntity;
import gamja.gamja_pre.domain.repository.PostRepository;
import gamja.gamja_pre.dto.request.PostRequest;
import gamja.gamja_pre.dto.response.PostResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional
public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Page : 전체 페이지 정보와 메타 데이터 제공. 총 페이지 수, 전체 데이터 수 등의 정보
    // Slice : 현재 페이지에 대한 데이터만 제공. 전체 페이지 수에 대한 정보는 포함되지 않음

    public Page<PostResponse> getAllPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostEntity> postEntityPage = postRepository.findAllByOrderByCreatedAtAsc(pageable);

        // Entity -> DTO 변환
        List<PostResponse> postRequests = postEntityPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(postRequests, pageable, postEntityPage.getTotalElements());
    }

    public Slice<PostResponse> getAllPostsBySlice(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<PostEntity> postEntitySlice = postRepository.findSliceByOrderByCreatedAtAsc(pageable);

        // Entity -> DTO 변환
        List<PostResponse> postRequests = postEntitySlice.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new SliceImpl<>(postRequests, pageable, postEntitySlice.hasNext());
    }

    // Entity -> Response 변환 메소드
    private PostResponse convertToDto(PostEntity postEntity) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(postEntity.getId());
        postResponse.setTitle(postEntity.getTitle());
        postResponse.setContent(postEntity.getContent());
        postResponse.setCreatedAt(postEntity.getCreatedAt());
        // 다른 필드도 설정
        return postResponse;
    }

    public PostResponse createPost(PostRequest post) throws Exception{
        // postRepository.save(post); 를 사용하지 않고 엔티티 객체를 생성한 후 저장하는 이유
        // 새롭게 엔티티 객체를 생성한 후 저장.
        // 이유 1: DTO(PostReqeust)와 엔티티(PostEntity) 분리.
        //      DTO: 클라이언트에서 전달받은 데이터를 담는 역햘, 엔티티: 실제로 데이터베이스에 저장되는 데이터 모델.
        //      DTO 와 엔티티는 서로 다른 목적을 갖기 때문에 클라이언트에서 받은 DTO 를 그대로 엔티티에 저장하지 않고 엔티티로 변환하는 과정이 필요.
        //      PostEntity.builder 를 사용해 엔티토로 변환 후 저장해야 함.
        // 이유 2: 불필요한 필드 노출 방지
        //      DTO 는 엔티티와 다른 필드나 구조를 가질 수 있음. ex) 비밀번호와 같은 민감한 정보를 엔티티에 그대로 저장하지 않고 해싱같은 처리를 거친 후 엔티티에 저장할 수 있음.
        // 이유 3: 여러 필드를 한 번에 설정하면서 가독성을 높여줌. 필수 필드만 설정하고 다른 필드를 설정할 수 있음.
        // 이유 4: 변환 로직의 명확화.
        //      DTO 의 필드를 엔티티의 필드로 매핑하는 변환 과정을 명확히 볼 수 있음. 로직이 명확해지고, 유지보수가 용이해짐.

        // DTO -> Entity 변환
        PostEntity postEntity = PostEntity.builder().
                title(post.getTitle()).
                content(post.getContent()).
                build();

        // 게시물 저장
        PostEntity savedPost = postRepository.save(postEntity);

        // 생성된 게시물 정보를 PostResponse DTO로 변환하여 반환
        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getCreatedAt());
    }

    public PostResponse updatePost(Long id, PostRequest post) throws Exception {
        PostEntity target = postRepository.findById(id).orElseThrow(() -> new Exception("User not found with id: " + id));

        target.setTitle(post.getTitle());
        target.setContent(post.getContent());

        postRepository.save(target);

        return new PostResponse(target.getId(), target.getTitle(), target.getContent(), target.getCreatedAt());
    }

    public PostResponse deletePost(Long id) throws Exception {
        PostEntity target = postRepository.findById(id).orElseThrow(() -> new Exception("User not found with id: " + id));
        postRepository.deleteById(id);

        return new PostResponse(target.getId(), target.getTitle(), target.getContent(), target.getCreatedAt());
    }
}
