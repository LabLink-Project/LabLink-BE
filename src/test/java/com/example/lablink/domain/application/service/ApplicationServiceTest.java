package com.example.lablink.domain.application.service;

import com.example.lablink.domain.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.domain.application.entity.Application;
import com.example.lablink.domain.application.entity.ApplicationViewStatusEnum;
import com.example.lablink.domain.application.entity.ApprovalStatusEnum;
import com.example.lablink.domain.application.repository.ApplicationRepository;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.entity.UserInfo;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.joda.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationService applicationService;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private GetStudyService getStudyService;
    @Mock
    private UserService userService;

//    ApplicationRequestDto applicationRequestDto = new ApplicationRequestDto(
//            "message",
//            "name",
//            "phone",
//            "address",
//            "detail",
//            "gender",
//            now()
//    );

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
//        @Test
//        @DisplayName("성공 - 신청서 작성")
//        void addApplication() {
//            // Given
//            Study study = new Study();
//            Long studyId = 1L;
//            String id = "1";
//            User user = new User();
//            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
//
//            UserInfo userInfo = new UserInfo();
//
//            Application application = new Application(user, studyId, "message", "enum1", "enum2");
//
//            given(userService.getUser(userDetails)).willReturn(user);
//            given(getStudyService.getStudy(studyId)).willReturn(study);
//            doNothing().when(user.getUserinfo()).updateUserInfo(applicationRequestDto);
//
//            // When
//            applicationService.addApplication(userDetails, studyId, applicationRequestDto);
//
//            // Then
//            verify(study).updateCurrentApplicantCount();
//            verify(user).updateUser(applicationRequestDto);
//            verify(user.getUserinfo()).updateUserInfo(applicationRequestDto);
//            verify(applicationRepository).save(application);
//        }
//@Test
//@DisplayName("성공 - 신청서 삭제")
//void deleteApplication() {
//    // Given
//    Long studyId = 1L;
//    Long applicationId = 1L;
//    String userId = "1";
//    User user = new User();
//    UserDetailsImpl userDetails = new UserDetailsImpl(user, userId);
//    Study study = new Study();
//    Application application = new Application(user, studyId, "message", "enum1", "enum2");
//
//    given(applicationRepository.findById(applicationId)).willReturn(Optional.of(application));
//
//    // When
//    applicationService.deleteApplication(userDetails, studyId, applicationId);
//
//    // Then
//    verify(applicationRepository).findById(applicationId);
//    verify(applicationRepository).delete(application);
//}




    }


    @Test
    void modifyApplication() {
    }

    @Test
    void deleteApplication() {
    }

    @Test
    void afterApplication() {
    }

    @Test
    void checkApplication() {
    }
}