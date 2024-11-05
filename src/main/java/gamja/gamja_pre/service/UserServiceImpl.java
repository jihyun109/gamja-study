package gamja.gamja_pre.service;

import gamja.gamja_pre.entity.UserEntity;
import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.dto.user.request.UserCreateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserUpdateRequestDTO;
import gamja.gamja_pre.dto.user.response.UserByIdResponseDTO;
import gamja.gamja_pre.dto.user.response.UserPagedListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserScrollListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserSearchByEmailResponseDTO;
import gamja.gamja_pre.error.ErrorCode;
import gamja.gamja_pre.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;    // 멤버 변수로 선언하고, 생성자를 통해 주입받음.

    @Override
    @Transactional(readOnly = true)
    public Page<UserPagedListResponseDTO> getPagedUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserEntity> userEntityPage = userRepository.findAllByOrderByIdAsc(pageable);

        List<UserPagedListResponseDTO> userRequests = mapToUserPagedListResponseDTO(userEntityPage.getContent());

        return new PageImpl<>(userRequests, pageable, userEntityPage.getTotalElements());
    }

    // List<UserEntity>를 List<UserPagedListResponseDTO> 로 변환
    private List<UserPagedListResponseDTO> mapToUserPagedListResponseDTO(List<UserEntity> userEntity) {
        return userEntity.stream()
                .map(this::convertToUserPagedListResponseDTO)
                .collect(Collectors.toList());
    }

    // Entity -> UserPagedListResponseDTO 변환
    private UserPagedListResponseDTO convertToUserPagedListResponseDTO(UserEntity userEntity) {
        return new UserPagedListResponseDTO(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getEmail()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public Slice<UserScrollListResponseDTO> getInfiniteScrollUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<UserEntity> userEntitySlice = userRepository.findSliceByOrderByIdAsc(pageable);

        List<UserScrollListResponseDTO> userRequests = mapToUserScrollListResponse(userEntitySlice.getContent());

        return new SliceImpl<>(userRequests, pageable, userEntitySlice.hasNext());
    }

    // List<UserEntity>를 List<UserScrollListResponseDTO> 로 변환
    private List<UserScrollListResponseDTO> mapToUserScrollListResponse(List<UserEntity> userEntity) {
        return userEntity.stream()
                .map(this::convertToUserScrollListResponseDTO)
                .collect(Collectors.toList());
    }

    // Entity -> UserScrollListResponseDTO 변환
    private UserScrollListResponseDTO convertToUserScrollListResponseDTO(UserEntity userEntity) {
        return new UserScrollListResponseDTO(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getEmail()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserByIdResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id, ErrorCode.NOT_FOUND));
        return new UserByIdResponseDTO(user.getId(), user.getUserName(), user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public UserSearchByEmailResponseDTO getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email, ErrorCode.NOT_FOUND));
        return new UserSearchByEmailResponseDTO(user.getId(), user.getUserName(), user.getEmail());
    }

    @Override
    public void createUser(UserCreateRequestDTO userCreateRequest) {
        UserEntity user = UserEntity.builder()
                .userName(userCreateRequest.getUserName())
                .email(userCreateRequest.getEmail())
                .password(userCreateRequest.getPassword())
                .build();
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserUpdateRequestDTO userUpdateRequest) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id, ErrorCode.NOT_FOUND));
        user.updateUser(userUpdateRequest.getUserName(), userUpdateRequest.getEmail());

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id, ErrorCode.NOT_FOUND));
        userRepository.deleteById(id);
    }
}
