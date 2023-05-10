package com.example.lablink.domain.user.security;

import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// 로그인시 유저 정보를 가저오는 역할 클래스
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /* jwt토큰 발급절차에서 subject의 내용이 달라질 경우 이 로직에서도 수정이 필요
     * email이 아니라 물리적인 id(PK)로 찾는게 맞음 -> 찾는 속도가 월등히 빠르고 효율적 */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(user, id);
    }

//    @Override
//    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
//        User user = userRepository.findById(id) .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        return new UserDetailsImpl(user, user.getEmail());
//    }
}