package gamja.gamja_pre.service;

import gamja.gamja_pre.entity.LikeEntity;
import gamja.gamja_pre.entity.PostEntity;
import gamja.gamja_pre.entity.UserEntity;
import gamja.gamja_pre.domain.id.LikeId;
import gamja.gamja_pre.repository.LikeRepository;
import gamja.gamja_pre.repository.PostRepository;
import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.dto.post.response.LikeByPostIdResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LikeByPostIdResponseDTO> getUserIdsByPostId(Long postId) {
        List<LikeEntity> likeEntityList = likeRepository.findUserIdsByPostId(postId);

        return mapToResponseDTO(likeEntityList, this::convertToLikeByPostIdResponseDTO);
    }

    private <T> List<T> mapToResponseDTO(List<LikeEntity> likeEntityList, Function<LikeEntity, T> mapper) {
        return likeEntityList.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private LikeByPostIdResponseDTO convertToLikeByPostIdResponseDTO(LikeEntity likeEntity) {
        return new LikeByPostIdResponseDTO(
                likeEntity.getSender().getId(),
                likeEntity.getSender().getUserName()
        );
    }

    @Override
    public void addLike(Long senderId, Long postId) {
        UserEntity user = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 사용자가 게시물에 좋아요를 누르지 않았을 경우 추가
        LikeId likeId = new LikeId(user.getId(), post.getId());
        if (!likeRepository.existsById(likeId)) {
            LikeEntity like = LikeEntity.builder()
                    .sender(user)
                    .post(post)
                    .build();
            likeRepository.save(like);
        }
    }

    @Override
    public void removeLike(Long senderId, Long postId) {
        LikeId likeId = new LikeId(senderId, postId);
        if (likeRepository.existsById(likeId)) {
            likeRepository.deleteById(likeId);
        } else {
            throw new RuntimeException("Like not found");
        }
    }
}
