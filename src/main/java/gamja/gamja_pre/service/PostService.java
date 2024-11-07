package gamja.gamja_pre.service;

import gamja.gamja_pre.dto.post.request.PostCreateRequestDTO;
import gamja.gamja_pre.dto.post.request.PostUpdateRequestDTO;
import gamja.gamja_pre.dto.post.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostService {
    Page<PostPagedListResponseDTO> getPagedPosts(int pageNumber, int pageSize);
    Slice<PostScrollListResponseDTO> getInfiniteScrollPosts(int pageNumber, int pageSize);

    PostByIdResponseDTO getPostById(Long id);
    List<PostSearchResponseDTO> getSearchByKeyword(String keyword);
    List<PostsByUserIdResponseDTO> getPostsByUserId(Long userId);
    void createPost(PostCreateRequestDTO postCreateRequest);
    void updatePost(Long id, PostUpdateRequestDTO postUpdateRequest);
    void deletePost(Long id);
    boolean isPostWriter(Long postId, String userName);
}
