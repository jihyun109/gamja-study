package gamja.gamja_pre.domain.service;

import gamja.gamja_pre.dto.user.request.UserCreateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserUpdateRequestDTO;
import gamja.gamja_pre.dto.user.response.UserPagedListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserResponseDTO;
import gamja.gamja_pre.dto.user.response.UserScrollListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public interface UserService {
    Page<UserPagedListResponseDTO> getPagedUsers(int pageNumber, int pageSize);
    Slice<UserScrollListResponseDTO> getInfiniteScrollUsers(int pageNumber, int pageSize);
    UserResponseDTO getUserById(Long id);
    void createUser(UserCreateRequestDTO userCreateRequest);
    void updateUser(Long id, UserUpdateRequestDTO userUpdateRequest);
    void deleteUser(Long id);
}
