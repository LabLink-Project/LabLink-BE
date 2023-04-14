package com.example.lablink.application.service;

import com.example.lablink.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.application.dto.Request.ApplicationStatusRequestDto;
import com.example.lablink.application.dto.Response.ApplicationResponseDto;
import com.example.lablink.application.entity.Application;
import com.example.lablink.application.entity.ApplicationViewStatusEnum;
import com.example.lablink.application.entity.ApprovalStatusEnum;
import com.example.lablink.application.exception.ApplicationErrorCode;
import com.example.lablink.application.exception.ApplicationException;
import com.example.lablink.application.repository.ApplicationRepository;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.responseDto.ApplicationFromStudyResponseDto;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.service.GetStudyService;
import com.example.lablink.study.service.StudyService;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import com.example.lablink.user.security.UserDetailsImpl;
import com.example.lablink.user.service.UserInfoService;
import com.example.lablink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final GetStudyService getStudyService;
    private final StudyService studyService;
    private final UserInfoService userInfoService;

    //신청서 추가
    @Transactional
    public void addApplication(UserDetailsImpl userDetails, Long studyId, ApplicationRequestDto applicationRequestDto) {
        // studyid들고와서 currentApplicantCount +1 해주기
        Study study = getStudyService.getStudy(studyId);
        study.updateCurrentApplicantCount();
        // 신청서 작성시 default -> 미열람, 승인 대기
        String applicationStatus = ApprovalStatusEnum.PENDING.toString();
        String applicationViewStatusEnum = ApplicationViewStatusEnum.NOT_VIEWED.toString();

        User user = userService.getUser(userDetails);
        // 신청서 작성시 회원가입에서 받지 않은 user정보 업데이트
        user.updateUser(applicationRequestDto);
        user.getUserinfo().updateUserInfo(applicationRequestDto);
        applicationRepository.save(new Application(userDetails.getUser(),studyId,applicationRequestDto.getMessage(), applicationStatus, applicationViewStatusEnum));
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

//    // 신청서 조회
    @Transactional
    public Application getApplication(UserDetailsImpl userDetails, CompanyDetailsImpl companyDetails, Long studyId, Long applicationId) {
        if (userDetails != null) {
            Application application = applicationRepository.findByIdAndUserEmail(applicationId, userDetails.getUser().getEmail()).orElseThrow(
                () -> new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
            );

            if (!application.getStudyId().equals(studyId) || !application.getUser().getId().equals(userDetails.getUser().getId())) {
                throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
            }
            return application;
        }
        if (companyDetails != null) {
            Application application = applicationRepository.findById(applicationId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
            );

            String applicationViewStatusEnum = ApplicationViewStatusEnum.VIEWED.toString();
            application.viewStatusUpdate(applicationViewStatusEnum);
            return application;

        } else {
            throw new ApplicationException(ApplicationErrorCode.NOT_HAVE_PERMISSION);
        }
    }

    @Transactional(readOnly = true)
    public ApplicationResponseDto afterApplication(UserDetailsImpl userDetails, Long studyId) {
        User user = userService.getUser(userDetails);
        Study study = getStudyService.getStudy(studyId);
        return new ApplicationResponseDto(study.getCompany(),study,user,user.getUserinfo());
    }

    // 신청서 승인, 거절
    @Transactional
    public Application applicationStatus(CompanyDetailsImpl companyDetails, ApplicationStatusRequestDto statusRequestDto, Long studyId, Long applicationId) {
        if (companyDetails != null) {
            // 공고, 신청서 찾기
            getStudyService.getStudy(studyId);
            Application application = applicationRepository.findById(applicationId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

            if(statusRequestDto.getApplicationStatus().equals("승인")) {
                application.statusUpdate(ApprovalStatusEnum.APPROVED.toString());
            } else if(statusRequestDto.getApplicationStatus().equals("거절")) {
                application.statusUpdate(ApprovalStatusEnum.REJECTED.toString());
            }
            return application;
        } else {
            // 인증된 회사 정보가 없는 경우, 예외 처리
            throw new ApplicationException(ApplicationErrorCode.NOT_HAVE_PERMISSION);
        }
    }

    // 공고별 신청서 확인
    @Transactional
    public List<ApplicationFromStudyResponseDto> applicationFromStudy(CompanyDetailsImpl companyDetails, Long studyId) {
        // 1. 기업이 작성한 공고의id를 사용해 새로운 공고에 저장. -> 로그인 기업이 작성한 공고
        Study study = studyService.findStudyFromCompany(studyId, companyDetails.getCompany());

        List<ApplicationFromStudyResponseDto> applicationDtos = new ArrayList<>();
        // 2. 1번의 공고id를 사용해 해당 공고의 신청서를 리스트에 저장.
        List<Application> applications = applicationRepository.findByStudyId(study.getId());

        for (Application application : applications) {
            User user = userService.findUser(application.getUser().getId());
            UserInfo userInfo = userInfoService.findUserInfo(user.getUserinfo().getId());
            applicationDtos.add(new ApplicationFromStudyResponseDto(user, userInfo, application));
        }

        return applicationDtos;
    }

    // 내가 쓴 신청서 확인
    public List<Application> findAllByMyApplication(User user) {
        return applicationRepository.findAllByUser(user);
    }

    // 신청서 삭제
    public void deleteApplication(Application application) {
        applicationRepository.delete(application);
    }

}
