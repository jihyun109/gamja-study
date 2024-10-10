package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.dto.post.response.LikeByPostIdResponseDTO;

import java.util.List;

public interface LikeService {
    List<LikeByPostIdResponseDTO> getUserIdsByPostId(Long postId);

    void addLike(Long userId, Long postId);

    void removeLike(Long userId, Long postId);
}
