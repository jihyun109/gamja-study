package gamja.gamja_pre.domain.controller;

import gamja.gamja_pre.domain.service.PostServiceImpl;
import gamja.gamja_pre.dto.post.request.PostCreateRequestDTO;
import gamja.gamja_pre.dto.post.request.PostUpdateRequestDTO;
import gamja.gamja_pre.dto.post.response.PostResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

// api
// 입력
// 동작
// 출력
// dto 를 사용 , == entity 를 외부로 노출하지마라,
@RestController // JSON, XML 과 같은 형식으로 데이터 반환
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postServiceImpl;

    @GetMapping("/posts") // 모든 post 리스트 조회
    public HashMap<String, Object> getAllPosts(@RequestParam(defaultValue = "slice") String viewType,  // 보기 방식
                                               @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
                                               @RequestParam(defaultValue = "6") int pageSize) {   // 페이지당 제공하는 게시물 수
        // 입력 - 보기방식, 페이지 번호, 페이지 당 제공하는 게시물 수 : requestParam 으로 받기
        // 동작 - 보기 방식에 따라 service 호출 / service 가 throw 하는 예외 처리
        // 출력 - 조회 성공 시: List<PostResponse> / 조회 실패 시 : result fail, 오류 출력

        // todo(done) : 에러 상황에 따라 상황에 따른 errorMessage 와 error code 응답 (마지막에 찾아보고 고치기) + 공통화

        HashMap<String, Object> result = new HashMap<>();
        if (viewType.equals("slice")) {
            // 무한 스크롤 페이지네이션
            result.put("result", "success");
            result.put("data", postServiceImpl.getInfiniteScrollPosts(pageNumber, pageSize));
        } else {
            // 한 페이지당 6개의 게시물
            result.put("result", "success");
            result.put("data", postServiceImpl.getPagedPosts(pageNumber, pageSize));
        }
        return result;
    }

    @GetMapping("/post/{id}")
    public PostResponseDTO getPostById(@PathVariable("id") Long id) {
        return postServiceImpl.getPostById(id);
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

    @PostMapping("/posts")    // post 생성
    public ResponseEntity<String> createPost(@Valid @RequestBody PostCreateRequestDTO postCreateRequest) {
        postServiceImpl.createPost(postCreateRequest);
        return ResponseEntity.ok("게시물 생성 완료");
    }

    @PutMapping("/posts/{id}")    // post 수정
    public ResponseEntity<String> updatePost(@PathVariable(required = true) Long id, @Valid @RequestBody PostUpdateRequestDTO postUpdateRequest) {
        postServiceImpl.updatePost(id, postUpdateRequest);
        return ResponseEntity.ok("게시물 수정 완료");
    }


    @DeleteMapping("/posts/{id}") // post 삭제
    public ResponseEntity<String> deletePost(@PathVariable(required = true) Long id) {
        postServiceImpl.deletePost(id);
        return ResponseEntity.ok("게시물 삭제 완료");
    }
}
