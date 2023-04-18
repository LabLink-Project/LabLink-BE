//package com.example.lablink.user.service;
//
//import com.example.lablink.user.dto.request.SignupRequestDto;
//import com.example.lablink.user.entity.Terms;
//import com.example.lablink.user.entity.User;
//import com.example.lablink.user.entity.UserInfo;
//import com.example.lablink.user.entity.UserRoleEnum;
//import com.example.lablink.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    // 임시의 Mock 객체를 생성할 때 import가 자동으로해당 service안에 있는 클래스들의 import? 의존성을 알아서 주입해준다 ??
//    // 테스트 코드를 작성할 Service 주입.
//    // setup메서드를 작성하지 않아도 된다
//    // setup메서드는 Mock객체를 인스턴스화 시키는 것 ?
//    @InjectMocks
//    private UserService userService;
//
//    // Mock으로 UserService에서 사용되는 클래스를 의존성 주입함.
//    // 테스트에서는 UserService의 JwtUtill과 같은 클래스를 주입받을 필요는 없다.
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private UserInfoService userInfoService;
//
//    @Mock
//    private TermsService termsService;
//
//    @Test
//    void signup() {
//
//
//        // given -> 기존 코드의 경우 클라이언트에서 데이터를 받아오지만 테스트에서는 생성자를 통해 지정함.
//        SignupRequestDto signupRequestDto = new SignupRequestDto(
//            "test01@naver.com",
//            "encodePassword",
//            "010-1111-1111",
//            true,
//            true,
//            true,
//            true,
//            false
//        );
//
//        User usersaved = new User(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), new UserInfo(), UserRoleEnum.USER);
//
//        // 아래의 userRepository.existsByEmail() 메서드 호출시 false반환 설정 -> 이메일 중복 X
//        // 아래의 given을 해주는 이유는 원본 UserService에서 호출되는 메서드가
//        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);
//        given(passwordEncoder.encode(signupRequestDto.getPassword())).willReturn("encodePassword");
//        given(userInfoService.saveUserInfo(signupRequestDto)).willReturn(new UserInfo());
//        given(userRepository.save(any(User.class))).willReturn(usersaved);
//        given(termsService.saveTerms(signupRequestDto, usersaved)).willReturn(new Terms());
//
//        // when ->  메서드를 변수화? -> then의 코드 실행에서 when을 사용한다.
//        // UserService에서 실행할 메서드(테스트할 메서드)를 변수에 담기?
//        String result = userService.signup(signupRequestDto);
//
//        // then -> when 몇번 실행할지,
//        // 검증 단계 -> 호출할 특정 메서드 지정 가능 및 횟수 설정 가능.
//        then(userRepository).should().existsByEmail(signupRequestDto.getEmail());
//        then(passwordEncoder).should(times(2)).encode(signupRequestDto.getPassword());
//        then(userInfoService).should().saveUserInfo(signupRequestDto);
//        then(userRepository).should().save(any(User.class));
//        then(termsService).should().saveTerms(signupRequestDto, usersaved);
//        assertEquals("회원가입 완료.", result);
//
//    }
//}