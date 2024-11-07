package gamja.gamja_pre.controller;

import gamja.gamja_pre.service.PostService;
import gamja.gamja_pre.dto.post.request.PostCreateRequestDTO;
import gamja.gamja_pre.dto.post.request.PostUpdateRequestDTO;
import gamja.gamja_pre.dto.post.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// api
// 입력
// 동작
// 출력
// dto 를 사용 , == entity 를 외부로 노출하지마라,
@RestController // JSON, XML 과 같은 형식으로 데이터 반환
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/paged") // 페이징 처리됨.
    public ResponseEntity<Page<PostPagedListResponseDTO>> getPagenatedPosts(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Page<PostPagedListResponseDTO> pagedUsers = postService.getPagedPosts(pageNumber, pageSize);
        return ResponseEntity.ok(pagedUsers);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/scroll") // 무한 스크롤
    public ResponseEntity<Slice<PostScrollListResponseDTO>> getInfiniteScrollPosts(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Slice<PostScrollListResponseDTO> scrollUsers = postService.getInfiniteScrollPosts(pageNumber, pageSize);
        return ResponseEntity.ok(scrollUsers);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{id}")
    public ResponseEntity<PostByIdResponseDTO> getPostById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

//    @PostMapping("/posts")    // post 생성
//    public HashMap<String, String> createPost(@RequestBody PostRequest post) {
//        // 입력 - PostRequest post: RequestBody 로 받기
//        // 동작 - post 생성: service 에서 처리 / service 가 throw 하는 예외 처리
//        // 출력 - 생성 성공 시: result success, data '생성한 post' / 생성 실패 시 : result fail, 오류 출력
//
//        HashMap<String, String> result = new HashMap<>();
//        result.put("result", "success");
//        result.put("data", postServiceImpl.createPost(post).toString());
//        return result;
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostSearchResponseDTO>> getSearchPosts(
            @RequestParam("keyword") String keyword) {
        List<PostSearchResponseDTO> posts = postService.getSearchByKeyword(keyword);
        return ResponseEntity.ok(posts);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<PostsByUserIdResponseDTO>> getSearchPosts(
            @PathVariable("userId") Long userId) {
        List<PostsByUserIdResponseDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts")    // post 생성
    public ResponseEntity<String> createPost(@Valid @RequestBody PostCreateRequestDTO postCreateRequest) {
        postService.createPost(postCreateRequest);
        return ResponseEntity.ok("게시물 생성 완료");
    }

    @PutMapping("/posts/{id}")    // post 수정

    @PreAuthorize("@postServiceImpl.isPostWriter(#id, authentication.name)")  // 게시물 수정 권한 체크
    public ResponseEntity<String> updatePost(@PathVariable(required = true) Long id, @Valid @RequestBody PostUpdateRequestDTO postUpdateRequest) {
        System.out.println("hi");
        postService.updatePost(id, postUpdateRequest);
        return ResponseEntity.ok("게시물 수정 완료");
    }


    @DeleteMapping("/posts/{id}") // post 삭제
    public ResponseEntity<String> deletePost(@PathVariable(required = true) Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("게시물 삭제 완료");
    }
}
