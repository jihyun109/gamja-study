package gamja.gamja_pre.domain.controller;

import gamja.gamja_pre.domain.service.LikeService;
import gamja.gamja_pre.dto.post.response.LikeByPostIdResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/post/{postId}/user-ids")
    public ResponseEntity<List<LikeByPostIdResponseDTO>> getUserIdsByPostId(@PathVariable Long postId) {
        List<LikeByPostIdResponseDTO> posts = likeService.getUserIdsByPostId(postId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<String> addLike(@RequestParam Long senderId, @RequestParam Long postId) {
        likeService.addLike(senderId, postId);
        return ResponseEntity.ok("like 등록 완료");
    }

    @DeleteMapping
    public ResponseEntity<String> removeLike(@RequestParam Long senderId, @RequestParam Long postId) {
        likeService.removeLike(senderId, postId);
        return ResponseEntity.ok("like 삭제 완료");
    }
}
