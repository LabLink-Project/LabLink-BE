package com.example.lablink.domain.application.service;

import com.example.lablink.domain.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.domain.application.dto.Response.ApplicationResponseDto;
import com.example.lablink.domain.application.entity.Application;
import com.example.lablink.domain.application.entity.ApplicationViewStatusEnum;
import com.example.lablink.domain.application.entity.ApprovalStatusEnum;
import com.example.lablink.domain.application.exception.ApplicationErrorCode;
import com.example.lablink.domain.application.exception.ApplicationException;
import com.example.lablink.domain.application.repository.ApplicationRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final GetStudyService getStudyService;

//    신청서 작성
    @Transactional
    public void addApplication(UserDetailsImpl userDetails, Long studyId, ApplicationRequestDto applicationRequestDto) {
        // studyid들고와서 currentApplicantCount +1 해주기
        Study study = getStudyService.getStudy(studyId);
        study.updateCurrentApplicantCount();
        User user = userService.getUser(userDetails);

        // 신청서 작성시 회원가입에서 받지 않은 user정보 업데이트
        user.updateUser(applicationRequestDto);
        user.getUserinfo().updateUserInfo(applicationRequestDto);

        // 신청서 작성시 default -> 미열람, 승인 대기
        Application application = new Application(
            user,
            studyId,
            applicationRequestDto.getMessage(),
            ApprovalStatusEnum.PENDING.toString(),
            ApplicationViewStatusEnum.NOT_VIEWED.toString()
        );

        applicationRepository.save(application);
    }

    //신청서 수정
    @Transactional
    public void modifyApplication(UserDetailsImpl userDetails, Long studyId,ApplicationRequestDto applicationRequestDto,Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
        );

        if(!application.getStudyId().equals(studyId) || !application.getUser().getId().equals(userDetails.getUser().getId())){
            throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
        }

        application.update(applicationRequestDto.getMessage());
    }
    //신청서 삭제
    @Transactional
    public void deleteApplication(UserDetailsImpl userDetails, Long studyId,Long applicationId) {
        Application application =applicationRepository.findById(applicationId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
        );

        if(!application.getStudyId().equals(studyId) || !application.getUser().getId().equals(userDetails.getUser().getId())){
          throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
        }
        applicationRepository.delete(application);
    }

//    // 유저의 신청서 조회
//    public ApplicationFromStudyResponseDto userDetailApplicationFromStudy(UserDetailsImpl userDetails, Long studyId, Long applicationId) {
//        Application application = applicationRepository.findByIdAndUserEmail(applicationId, userDetails.getUser().getEmail()).orElseThrow(
//            () -> new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
//        );
//
//        if (!application.getStudyId().equals(studyId) || !application.getUser().getId().equals(userDetails.getUser().getId())) {
//            throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
//        }
//        ApplicationFromStudyResponseDto dto = new ApplicationFromStudyResponseDto(userDetails.getUser(), userDetails.getUser().getUserinfo(), application);
//        return dto;
//    }

    //신청서 접수 클릭 시 나오는 정보 값
    @Transactional(readOnly = true)
    public ApplicationResponseDto afterApplication(UserDetailsImpl userDetails, Long studyId) {
        User user = userService.getUser(userDetails);
        Study study = getStudyService.getStudy(studyId);
        return new ApplicationResponseDto(study.getCompany(),study,user,user.getUserinfo());
    }

    public boolean checkApplication(Long studyId, User user) {
        return applicationRepository.existsByStudyIdAndUser(studyId, user);
    }
}
