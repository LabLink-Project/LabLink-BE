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
//    void testEmailCheck() throws Exception {
//        // given
//        UserEmailCheckRequestDto userEmailCheckRequestDto = new UserEmailCheckRequestDto("test@example.com");
//        given(userRepository.existsByEmail(userEmailCheckRequestDto.getEmail())).willReturn(false);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post("/users/signup/email-check")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(new ObjectMapper().writeValueAsString(userEmailCheckRequestDto)));
//
//        // then
////        resultActions
////            .andExpect(status().isConflict())
//
////        // then
//        resultActions
//            .andExpect(status().isBadRequest());
//    }

//    @Test
//    void 이메일_중복_체크() throws Exception {
//        // given
//        UserEmailCheckRequestDto userEmailCheckRequestDto = new UserEmailCheckRequestDto("test0001@naver.com");
//        given(userService.emailCheck(any(UserEmailCheckRequestDto.class))).willReturn("사용 가능합니다.");
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post("/users/signup/email-check")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(new ObjectMapper().writeValueAsString(userEmailCheckRequestDto)));
//        System.out.println(userService.emailCheck(userEmailCheckRequestDto));
//        // then
//        resultActions
//            .andExpect(jsonPath("$.statusCode").value(200))
//            .andExpect(jsonPath("$.message").value("사용 가능합니다."))
//            .andDo(print())
//            .andExpect(jsonPath("$.data").value(""));
//    }

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
