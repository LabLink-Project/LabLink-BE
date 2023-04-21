//package com.example.lablink.user.controller;
//
//import com.example.lablink.user.dto.request.LoginRequestDto;
//import com.example.lablink.user.dto.request.SignupRequestDto;
//import com.example.lablink.user.repository.UserRepository;
//import com.example.lablink.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import org.springframework.test.web.servlet.ResultActions;
//
//import javax.servlet.http.HttpServletResponse;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//
////@ExtendWith(MockitoExtension.class)
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    private MockMvc mockMvc;
//
//    private SignupRequestDto signupRequestDto = new SignupRequestDto(
//        "test0001@naver.com",
//        "nick",
//        "password",
//        "010-1111-1111",
//        true,
//        true,
//        true,
//        true,
//        false
//    );
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void 회원가입() throws Exception {
//        // given
//
//        // any -> 인자가 SignupRequestDto타입인 모든 객체
//        // userService.signup()메서드 호출시 "회원가입 완료." 반환
//        given(userService.signup(any(SignupRequestDto.class))).willReturn("회원가입 완료.");
//
//        // when
//        // 메서드 방식과 앤드포인트 지정.
//        ResultActions resultActions = mockMvc.perform(post("/users/signup")
//        // 요청 타입이 json이다.
//            .contentType(MediaType.APPLICATION_JSON)
//        // json 타입인 signupRequestDto를 문자열로 변환.
//            .content(new ObjectMapper().writeValueAsString(signupRequestDto)));
//
//        // 간단히 통과 & json 타입인지 검증할 때 사용 가능 -> 커버리지는 아래의 then을 사용하는게 더 높다
//        // then
////        resultActions
////            .andExpect(status().isOk())
////            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        resultActions
//            .andExpect(jsonPath("$.statusCode").value(200))
//            .andExpect(jsonPath("$.message").value("회원가입 완료."))
//            .andExpect(jsonPath("$.data").value(""));
//    }
//
//    @Test
//    void 로그인() throws Exception {
//        // given
//        LoginRequestDto loginRequestDto = new LoginRequestDto("test0001@naver.com", "password");
//        given(userService.login(any(LoginRequestDto.class), any(HttpServletResponse.class))).willReturn("로그인 완료.");
//        // when
//        ResultActions resultActions = mockMvc.perform(post("/users/login")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(new ObjectMapper().writeValueAsString(loginRequestDto)));
//        // then
//        resultActions
//            .andExpect(jsonPath("$.statusCode").value(200))
//            .andExpect(jsonPath("$.message").value("로그인 완료."))
//            .andExpect(jsonPath("$.data").value(""));
//    }
//
////    @Test
////    void testEmailCheck() throws Exception {
////        // given
////        UserEmailCheckRequestDto userEmailCheckRequestDto = new UserEmailCheckRequestDto("test@example.com");
////        given(userRepository.existsByEmail(userEmailCheckRequestDto.getEmail())).willReturn(false);
////
////        // when
////        ResultActions resultActions = mockMvc.perform(post("/users/signup/email-check")
////            .contentType(MediaType.APPLICATION_JSON)
////            .content(new ObjectMapper().writeValueAsString(userEmailCheckRequestDto)));
////
////        // then
//////        resultActions
//////            .andExpect(status().isConflict())
////
//////        // then
////        resultActions
////            .andExpect(status().isBadRequest());
////    }
//
////    @Test
////    void 이메일_중복_체크() throws Exception {
////        // given
////        UserEmailCheckRequestDto userEmailCheckRequestDto = new UserEmailCheckRequestDto("test0001@naver.com");
////        given(userService.emailCheck(any(UserEmailCheckRequestDto.class))).willReturn("사용 가능합니다.");
////
////        // when
////        ResultActions resultActions = mockMvc.perform(post("/users/signup/email-check")
////            .contentType(MediaType.APPLICATION_JSON)
////            .content(new ObjectMapper().writeValueAsString(userEmailCheckRequestDto)));
////        System.out.println(userService.emailCheck(userEmailCheckRequestDto));
////        // then
////        resultActions
////            .andExpect(jsonPath("$.statusCode").value(200))
////            .andExpect(jsonPath("$.message").value("사용 가능합니다."))
////            .andDo(print())
////            .andExpect(jsonPath("$.data").value(""));
////    }
//
//    @Test
//    void 회원탈퇴() throws Exception {
//
//    }
//
//    @Test
//    void 내_실험_관리() throws Exception {
//
//    }
//}
