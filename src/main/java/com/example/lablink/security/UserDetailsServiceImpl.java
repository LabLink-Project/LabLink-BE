package com.example.lablink.security;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.repository.CompanyRepository;
import com.example.lablink.company.service.CompanyService;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import com.example.lablink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// 로그인시 유저 정보를 가저오는 역할 클래스
public class UserDetailsServiceImpl implements UserDetailsService {


//    private final UserService userService;
    private final UserRepository userRepository;
//    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @Override
    /* jwt토큰 발급절차에서 subject의 내용이 달라질 경우 이 로직에서도 수정이 필요
     * email이 아니라 물리적인 id(PK)로 찾는게 맞음 -> 찾는 속도가 월등히 빠르고 효율적 */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userService.findByUserEmail(email);
        User user = userRepository.findByEmail(email) .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        Company company = companyService.findByCompanyEmail(email);
        Company company = companyRepository.findByEmail(email) .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 둘 다 null이 아니고 이메일이 같을 경우 중복 에러 발생
        if (user != null && company != null) {
            if (user.getEmail().equals(company.getEmail())) {
                throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
            }
        }

        // 둘 다 null일 때 에러 발생
        if (user == null && company == null) {
            throw new UserException(UserErrorCode.EMAIL_NOT_FOUND);
        }

//        if () {
//
//        }
        return new UserDetailsImpl(user, company, email);
    }
    // TODO: 1. if로 유저 컴퍼니 뭘 보낼지 정하기 -> null값이 아닐때 리턴?
    // TODO: . 유저 이메일과 컴퍼니 이메일이 같으면? -> user와 company에서 find 함수를 만들어 가져와 .getEmail해서 조건문??
    // TODO: 고유값인 id로 찾는다면 이메일 중복 이슈는 없지 않을까?
}