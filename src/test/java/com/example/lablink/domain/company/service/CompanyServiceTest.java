package com.example.lablink.domain.company.service;

import com.example.lablink.domain.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.domain.company.dto.request.CompanyNameCheckRequestDto;
import com.example.lablink.domain.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.domain.company.dto.response.ViewMyStudyResponseDto;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.company.repository.CompanyRepository;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.StudyService;
import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.global.S3Image.entity.S3Image;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.exception.GlobalErrorCode;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private StudyService studyService;
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

        @Test
        @DisplayName("성공 - 기업 회원 탈퇴")
        void deleteCompany() {
            Company company = new Company();
            String id = "1";
            Study study = new Study();
            CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(company, id);
            List<Study> studies = Collections.singletonList(study);
//            List<Study> studies = new ArrayList<>();
            given(studyService.findAllCompanyStudy(company)).willReturn(studies);

            // when
            companyService.deleteCompany(companyDetails, response);
            // given
            verify(studyService).deleteStudy(study.getId(), companyDetails);
            verify(companyRepository).delete(company);
            verify(response).setHeader(eq(JwtUtil.AUTHORIZATION_HEADER), isNull());
        }

        @Test
        @DisplayName("성공 - 내 공고 확인")
        void viewMyStudies() {
            // given
            Company company = new Company();
            String id = "1";
            CompanyDetailsImpl companyDetails = new CompanyDetailsImpl(company, id);
            List<Study> studies = new ArrayList<>();
            List<ViewMyStudyResponseDto> views = new ArrayList<>();
//            for (Study study : studies) {
//                views.add(new ViewMyStudyResponseDto(study));
//            }
            given(studyService.findAllCompanyStudy(company)).willReturn(studies);

            // when
            List<ViewMyStudyResponseDto> viewMyStudies = companyService.viewMyStudies(companyDetails);
            // then
            assertEquals(viewMyStudies, views);
        }

        @Test
        @DisplayName("성공 - 이메일 중복x 사용 가능")
        void checkEmail() {
            String email = companySignupRequestDto.getEmail();
            given(companyRepository.existsByEmail(email)).willReturn(false);
            // when & then
            companyService.checkEmail(email);
        }

        @Test
        @DisplayName("성공 - 이메일 존재 확인")
        void existEmail() {
            String email = companySignupRequestDto.getEmail();
            given(companyRepository.existsByEmail(email)).willReturn(false);
            // when
            boolean result = companyService.existEmail(email);
            // then
            assertFalse(result);
        }
    } // 성공 케이스

    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
        @Test
        @DisplayName("실패 - 유저와 이메일 중복")
        void companySignupDuplicateEmail() {
            // given
            String email = companySignupRequestDto.getEmail();
            given(userServiceProvider.get()).willReturn(userService);
            given(userService.existEmail(email)).willReturn(true);

            // when & then
            assertThrows(GlobalException.class, () -> {
                companyService.companySignup(companySignupRequestDto, s3ResponseDto);
            }, "중복된 이메일이 존재합니다.");
        }
        @Test
        @DisplayName("실패 - 중복된 회사 이름")
        void duplicateCompanyName() {
            // given
            String companyName = companySignupRequestDto.getCompanyName();
            given(userServiceProvider.get()).willReturn(userService);
            given(companyRepository.existsByCompanyName(companyName)).willReturn(true);

            // when & then
            GlobalException exception = assertThrows(GlobalException.class, () -> {
                companyService.companySignup(companySignupRequestDto, s3ResponseDto);
            });
            assertEquals(GlobalErrorCode.DUPLICATE_COMPANY_NAME, exception.getErrorCode());
        }





    } // 실패 케이스










}