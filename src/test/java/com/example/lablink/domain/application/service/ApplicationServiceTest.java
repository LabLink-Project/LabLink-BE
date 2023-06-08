package com.example.lablink.domain.application.service;

import com.example.lablink.domain.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.domain.application.dto.Response.ApplicationResponseDto;
import com.example.lablink.domain.application.entity.Application;
import com.example.lablink.domain.application.repository.ApplicationRepository;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.entity.UserInfo;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService applicationService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private GetStudyService getStudyService;
    @Mock
    private UserService userService;
    @Mock
    private ApplicationRequestDto applicationRequestDto;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
//        @Test
//        @DisplayName("성공 - 신청서 작성")
//        void addApplication() {
//            // 테스트에 필요한 객체들을 생성하고 초기화합니다.
//            Long studyId = 1L;  // 스터디 ID
//            String id = "1";
//            User user = new User();
//            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
//            Study study = new Study();
//            UserInfo userInfo = new UserInfo();
//            Application application = new Application(user, studyId, "message", "enum1", "enum2");
//            given(userService.getUser(userDetails)).willReturn(user);
//
//            // user.setUserinfo()를 호출하여 UserInfo 객체를 생성하고 연결해줍니다.
//            user.setUserinfo(userInfo);
//
//            // 테스트할 메서드를 호출합니다.
//            applicationService.addApplication(userDetails, studyId, applicationRequestDto);
//
//            verify(study).updateCurrentApplicantCount();
//            verify(user).updateUser(applicationRequestDto);
//            verify(user.getUserinfo()).updateUserInfo(applicationRequestDto);
//            verify(applicationRepository).save(application);
//        }
        @Test
        @DisplayName("성공 - 신청서 삭제")
        void deleteApplication() {
            // Given
            String id = "1";
            Long studyId = 1L;
            Long applicationId = 1L;
            Long userId = 1L;
            User user = new User();
            user.setId(userId); // Set the user ID
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
            Application application = new Application(user, studyId, "message", "enum1", "enum2");

            given(applicationRepository.findById(applicationId)).willReturn(Optional.of(application));

            // When
            applicationService.deleteApplication(userDetails, studyId, applicationId);
            // Then
            verify(applicationRepository).delete(application);
        }
        @Test
        @DisplayName("성공 - 신청서 확인")
        void checkApplication() {
            Long studyId = 1L;
            User user = new User();
            // when
            applicationService.checkApplication(studyId, user);
            // then
            verify(applicationRepository).existsByStudyIdAndUser(studyId, user);
        }
        @Test
        @DisplayName("성공 - 신청서 접수 ")
        void afterApplication() {
            User user = new User();
            Study study = new Study();
            Company company = new Company();
            UserInfo userInfo = new UserInfo();
            ApplicationResponseDto applicationResponseDto = new ApplicationResponseDto(company, study, user, userInfo);
            String id = "1";
            Long studyId = 1L;
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
            given(userService.getUser(userDetails)).willReturn(user);
            given(getStudyService.getStudy(studyId)).willReturn(study);

            // when
            ApplicationResponseDto response = applicationService.afterApplication(userDetails, studyId);
            // then
            assertEquals(applicationResponseDto, response);
        }

    }

    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
        @Test
        @DisplayName("실패 - 신청서 삭제 - 신청서 존재x")
        void deleteApplication_ApplicationNotFound() {
            // Given
            String id = "1";
            Long studyId = 1L;
            Long applicationId = 1L;
            Long userId = 1L;
            User user = new User();
            user.setId(userId); // Set the user ID
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);

            given(applicationRepository.findById(applicationId)).willReturn(Optional.empty());

            // When/Then
            assertThrows(GlobalException.class, () -> applicationService.deleteApplication(userDetails, studyId, applicationId));
            verify(applicationRepository, never()).delete(any(Application.class));
        }
        @Test
        @DisplayName("실패 - 신청서 삭제 - 권한 없음")
        void deleteApplication_NotAuthorized() {
            // Given
            String id = "1";
            Long studyId = 1L;
            Long applicationId = 1L;
            Long userId = 1L;
            User user = new User();
            user.setId(userId); // Set the user ID
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
            Application application = new Application(user, studyId, "message", "enum1", "enum2");

            given(applicationRepository.findById(applicationId)).willReturn(Optional.of(application));

            // When/Then
            assertThrows(GlobalException.class, () -> applicationService.deleteApplication(userDetails, studyId + 1, applicationId));
            assertThrows(GlobalException.class, () -> applicationService.deleteApplication(userDetails, studyId, applicationId + 1));
            verify(applicationRepository, never()).delete(any(Application.class));
        }
    } // Failclass



    @Test
    void modifyApplication() {
    }

    @Test
    void deleteApplication() {
    }




}