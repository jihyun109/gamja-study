package gamja.gamja_pre.domain.controller;

import gamja.gamja_pre.domain.service.UserServiceImpl;
import gamja.gamja_pre.dto.post.request.PostUpdateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserCreateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserUpdateRequestDTO;
import gamja.gamja_pre.dto.user.response.UserPagedListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserResponseDTO;
import gamja.gamja_pre.dto.user.response.UserScrollListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/users/paged") // 페이징 처리됨.
    public ResponseEntity<Page<UserPagedListResponseDTO>> getPagenatedUsers(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Page<UserPagedListResponseDTO> pagedUsers = userServiceImpl.getPagedUsers(pageNumber, pageSize);
        return ResponseEntity.ok(pagedUsers);
    }

    @GetMapping("/users/scroll") // 무한 스크롤
    public ResponseEntity<Slice<UserScrollListResponseDTO>> getInfiniteScrollUsers(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Slice<UserScrollListResponseDTO> scrollUsers = userServiceImpl.getInfiniteScrollUsers(pageNumber, pageSize);
        return ResponseEntity.ok(scrollUsers);
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }
    
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        userServiceImpl.createUser(userCreateRequestDTO);
        return ResponseEntity.ok("유저 생성 완료");
    }

    @PutMapping("/users/{id}")    // user 수정
    public ResponseEntity<String> updateUser(@PathVariable(required = true) Long id, @RequestBody UserUpdateRequestDTO userUpdateRequest) {
        userServiceImpl.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok("유저 수정 완료");
    }


    @DeleteMapping("/users/{id}") // user 삭제
    public ResponseEntity<String> deleteUser(@PathVariable(required = true) Long id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.ok("유저 삭제 완료");
    }
}
