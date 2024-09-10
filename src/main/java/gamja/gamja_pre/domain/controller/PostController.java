package gamja.gamja_pre.domain.controller;

import gamja.gamja_pre.domain.service.PostService;
import gamja.gamja_pre.dto.request.PostRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

// api
// 입력
// 동작 - 단순 repo method 만 호출하면 끝?
// 출력
// dto 를 사용 , == entity 를 외부로 노출하지마라,
@RestController // JSON, XML 과 같은 형식으로 데이터 반환
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

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
            result.put("data", postService.getAllPostsBySlice(pageNumber, pageSize));
        } else {
            // 한 페이지당 6개의 게시물
            result.put("result", "success");
            result.put("data", postService.getAllPosts(pageNumber, pageSize));
        }
        return result;
    }

    @PostMapping("/posts")    // post 생성
    public HashMap<String, String> createPost(@RequestBody PostRequest post) {
        // 입력 - PostRequest post: RequestBody 로 받기
        // 동작 - post 생성: service 에서 처리 / service 가 throw 하는 예외 처리
        // 출력 - 생성 성공 시: result success, data '생성한 post' / 생성 실패 시 : result fail, 오류 출력

        HashMap<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("data", postService.createPost(post).toString());
        return result;
    }

    @PutMapping("/posts/{id}")    // post 수정
    public HashMap<String, String> updatePost(@PathVariable(required = true) Long id, @RequestBody PostRequest post) {
        // 입력 - 수정될 post: RequestBody 로 받기. / id : path 로 받기
        // 동작 - post 수정: service 에서 처리 / service 가 throw 하는 예외 처리
        // 출력 - 수정 성공 시 : result success, data '수정된 post' / 수정 실패 시 : result fail, 오류 출력

        HashMap<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("data", postService.updatePost(id, post).toString());
        return result;
    }

    @DeleteMapping("/posts/{id}") // post 삭제
    public HashMap<String, String> deletePost(@PathVariable(required = true) Long id) {
        // 입력 - id: path 로 받기
        // 동작 - post 삭제: service 에서 처리 / service 가 throw 하는 예외 처리
        // 출력 - 삭제 성공 시: result success, data '삭제한 post' / 삭제 실패 시 : result fail, 오류 출력

        HashMap<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("data", postService.deletePost(id).toString());
        return result;
    }
}
