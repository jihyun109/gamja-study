package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.dto.post.request.PostCreateRequestDTO;
import gamja.gamja_pre.dto.post.request.PostUpdateRequestDTO;
import gamja.gamja_pre.dto.post.response.PostPagedListResponseDTO;
import gamja.gamja_pre.dto.post.response.PostResponseDTO;
import gamja.gamja_pre.dto.post.response.PostScrollListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface PostService {
    Page<PostPagedListResponseDTO> getPagedPosts(int pageNumber, int pageSize);

    Slice<PostScrollListResponseDTO> getInfiniteScrollPosts(int pageNumber, int pageSize);

    PostResponseDTO getPostById(Long id);
    void createPost(PostCreateRequestDTO postCreateRequest);
    void updatePost(Long id, PostUpdateRequestDTO postUpdateRequest);
    void deletePost(Long id);
}
