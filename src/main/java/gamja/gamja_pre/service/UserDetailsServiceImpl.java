package gamja.gamja_pre.service;

import gamja.gamja_pre.entity.UserEntity;
import gamja.gamja_pre.repository.UserRepository;
import gamja.gamja_pre.security.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userData = userRepository.findUserEntityByUserName(username);

        if (userData.isPresent()) {
            return new CustomUserDetails(userData.get());
        } else {
            throw new UsernameNotFoundException("Invalid Username.");
        }
    }
}
