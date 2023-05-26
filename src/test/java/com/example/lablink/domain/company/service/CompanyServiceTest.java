package com.example.lablink.domain.company.service;

import com.example.lablink.domain.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.domain.company.dto.request.CompanyNameCheckRequestDto;
import com.example.lablink.domain.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.company.repository.CompanyRepository;
import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.global.S3Image.entity.S3Image;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.exception.GlobalException;
import com.example.lablink.global.jwt.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Provider<UserService> userServiceProvider;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private HttpServletResponse response;
//    @Mock
//    private S3Image s3Image;

    S3Image s3Image = new S3Image(
            1L,
            "originfileName",
            "uploadFileName",
            "uploadFilePath",
            "uploadFileUrl"
    );
    CompanySignupRequestDto companySignupRequestDto = new CompanySignupRequestDto(
            "company@naver.com",
            "password01",
            "companyName",
            new MockMultipartFile("logo", new byte[0]),
            "ownerName",
            "business",
            "01033333333",
            "address",
            "detailAddress"
    );
    S3ResponseDto s3ResponseDto = new S3ResponseDto(s3Image);


    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        @DisplayName("성공 - 기업 회원가입")
        void companySignup() {
            // given
            given(userServiceProvider.get()).willReturn(userService);
            // when
            companyService.companySignup(companySignupRequestDto, s3ResponseDto);
            // then
            verify(companyRepository).save(any(Company.class));
        }
        @Test
        @DisplayName("성공 - 기업 로그인")
        void companyLogin() {
            // Given
            CompanyLoginRequestDto companyLoginRequestDto = new CompanyLoginRequestDto();
            String email = companyLoginRequestDto.getEmail();
            String password = companyLoginRequestDto.getPassword();
            String auth = JwtUtil.AUTHORIZATION_HEADER;
            Company company = new Company();
            company.setPassword(passwordEncoder.encode(password));

            given(companyRepository.findByEmail(email)).willReturn(Optional.of(company));
            given(passwordEncoder.matches(password, company.getPassword())).willReturn(true);

            // When
            companyService.companyLogin(companyLoginRequestDto, response);
            // Then
            verify(response).addHeader(auth, jwtUtil.createCompanyToken(company));
        }
        @Test
        @DisplayName("성공 - 이메일 중복 검사")
        void emailCheck() {
            // Given
            SignupEmailCheckRequestDto signupEmailCheckRequestDto = new SignupEmailCheckRequestDto("email@asdf123");
            String companyEmail = signupEmailCheckRequestDto.getEmail();
            String userEmail = "email@asdf123";
            given(userServiceProvider.get()).willReturn(userService);

            // When
            companyService.emailCheck(signupEmailCheckRequestDto);
            // Then
            assertEquals(companyEmail, userEmail);
        }
        @Test
        @DisplayName("성공 - 기업명 중복 x")
        void companyNameCheck() {
            // given
            CompanyNameCheckRequestDto companyNameCheckRequestDto = new CompanyNameCheckRequestDto();
            String companyName = companyNameCheckRequestDto.getCompanyName();
            given(companyRepository.existsByCompanyName(companyName)).willReturn(true);
            //when
            Assertions.assertThrows(GlobalException.class, () -> companyService.companyNameCheck(companyNameCheckRequestDto));
        }


    }



    @Test
    @DisplayName("")
    void deleteCompany() {
    }

    @Test
    @DisplayName("")
    void viewMyStudies() {
    }

    @Test
    @DisplayName("")
    void checkEmail() {
    }

    @Test
    @DisplayName("")
    void existEmail() {
    }
}