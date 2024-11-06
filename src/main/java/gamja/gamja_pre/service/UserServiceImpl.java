package gamja.gamja_pre.service;

import gamja.gamja_pre.dto.security.request.AuthUserAuthRequestDTO;
import gamja.gamja_pre.dto.security.request.CheckOtpDTO;
import gamja.gamja_pre.entity.UserEntity;
import gamja.gamja_pre.entity.security.AuthOtpEntity;
import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.dto.user.request.UserCreateRequestDTO;
import gamja.gamja_pre.dto.user.request.UserUpdateRequestDTO;
import gamja.gamja_pre.dto.user.response.UserByIdResponseDTO;
import gamja.gamja_pre.dto.user.response.UserPagedListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserScrollListResponseDTO;
import gamja.gamja_pre.dto.user.response.UserSearchByEmailResponseDTO;
import gamja.gamja_pre.error.ErrorCode;
import gamja.gamja_pre.error.NotFoundException;
import gamja.gamja_pre.repository.security.AuthOtpRepository;
import gamja.gamja_pre.security.util.GenerateCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;    // 멤버 변수로 선언하고, 생성자를 통해 주입받음.
    private final PasswordEncoder passwordEncoder;
    private final AuthOtpRepository authOtpRepository;

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
    public void addUser(UserCreateRequestDTO userCreateRequest) {
        UserEntity user = UserEntity.builder()
                .userName(userCreateRequest.getUserName())
                .email(userCreateRequest.getEmail())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
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

    @Override
    public void auth(AuthUserAuthRequestDTO userAuthRequestDTO) {
        Optional<UserEntity> optionalAuthUser = // db에서 사용자 검색
                userRepository.findUserEntityByUserName(userAuthRequestDTO.getUserName());

        if (optionalAuthUser.isPresent()) {  // 사용자가 있으면 암호 확인
            UserEntity user = optionalAuthUser.get();
            if (passwordEncoder.matches(userAuthRequestDTO.getPassword(), user.getPassword())) {    // 암호가 맞으면 새 OTP 생성
                renewOtp(user);
            } else {
                throw new BadCredentialsException("Bad credentials. Invalid username or password.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials. User not found.");
        }
    }

    // 사용자의 OTP 검증 메서드
    @Override
    public boolean check(CheckOtpDTO checkOtpDTO) {
        Optional<AuthOtpEntity> userOtp = authOtpRepository.findAuthOtpEntityByUserName(checkOtpDTO.getUserName()); // 사용자 OTP 검색

        // db에 OTP 가 있고 받은 OTP와 일치하면 true 반환
        if (userOtp.isPresent()) {
            AuthOtpEntity otp = userOtp.get();
            if (checkOtpDTO.getCode().equals(otp.getCode())) {
                return true;
            }
        }
        return false;
    }

    private void renewOtp(UserEntity user) {
        String code = GenerateCodeUtil.generateCode();  // OTP를 위한 임의의 수 생성
        Optional<AuthOtpEntity> userOtp = authOtpRepository.findAuthOtpEntityByUserName(user.getUserName());    // 사용자 이름으로 OTP 검색

        if (userOtp.isPresent()) {
            AuthOtpEntity otp = userOtp.get();
            AuthOtpEntity updatedOtp = AuthOtpEntity.builder()
                    .userName(otp.getUserName())  // 기존 사용자 이름 유지
                    .code(code)  // 새로운 OTP 코드 설정
                    .build();
            authOtpRepository.save(updatedOtp); // 업데이트된 OTP 저장
        } else {
            AuthOtpEntity newOtp = AuthOtpEntity.builder()
                    .userName(user.getUserName())
                    .code(code)
                    .build();
            authOtpRepository.save(newOtp);
        }
    }
}
