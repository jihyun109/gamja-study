package gamja.gamja_pre.controller;

import gamja.gamja_pre.service.UserService;
import gamja.gamja_pre.dto.user.request.UserCreateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserUpdateRequestDTO;
import gamja.gamja_pre.dto.user.response.UserPagedListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserByIdResponseDTO;
import gamja.gamja_pre.dto.user.response.UserScrollListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserSearchByEmailResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/paged") // 페이징 처리됨.
    public ResponseEntity<Page<UserPagedListResponseDTO>> getPagenatedUsers(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Page<UserPagedListResponseDTO> pagedUsers = userService.getPagedUsers(pageNumber, pageSize);
        return ResponseEntity.ok(pagedUsers);
    }

    @GetMapping("/users/scroll") // 무한 스크롤
    public ResponseEntity<Slice<UserScrollListResponseDTO>> getInfiniteScrollUsers(
            @RequestParam(defaultValue = "0") int pageNumber, // 페이지 번호
            @RequestParam(defaultValue = "6") int pageSize) {

        Slice<UserScrollListResponseDTO> scrollUsers = userService.getInfiniteScrollUsers(pageNumber, pageSize);
        return ResponseEntity.ok(scrollUsers);
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<UserByIdResponseDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserSearchByEmailResponseDTO> getUserById(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        userService.createUser(userCreateRequestDTO);
        return ResponseEntity.ok("유저 생성 완료");
    }

    @PutMapping("/users/{id}")    // user 수정
    public ResponseEntity<String> updateUser(@PathVariable(required = true) Long id, @Valid @RequestBody UserUpdateRequestDTO userUpdateRequest) {
        userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok("유저 수정 완료");
    }


    @DeleteMapping("/users/{id}") // user 삭제
    public ResponseEntity<String> deleteUser(@PathVariable(required = true) Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("유저 삭제 완료");
    }
}
