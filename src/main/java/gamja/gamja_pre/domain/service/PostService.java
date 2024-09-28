package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.dto.post.response.PostResponseDTO;

public interface PostService {
    PostResponseDTO getPostById(Long id);
}
