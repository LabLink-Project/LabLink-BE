package com.example.lablink.domain.application.service;

import com.example.lablink.domain.application.dto.Request.ApplicationStatusRequestDto;
import com.example.lablink.domain.application.dto.Response.ApplicationFromStudyResponseDto;
import com.example.lablink.domain.application.entity.Application;
import com.example.lablink.domain.application.entity.ApplicationViewStatusEnum;
import com.example.lablink.domain.application.entity.ApprovalStatusEnum;
import com.example.lablink.domain.application.exception.ApplicationErrorCode;
import com.example.lablink.domain.application.exception.ApplicationException;
import com.example.lablink.domain.application.repository.ApplicationRepository;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.domain.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationCompanyService {
    private final ApplicationRepository applicationRepository;
    private final GetStudyService getStudyService;
    private final StudyService studyService;

    // 기업의 신청서 조회
    @Transactional
    public ApplicationFromStudyResponseDto companyDetailApplicationFromStudy(CompanyDetailsImpl companyDetails, Long studyId, Long applicationId) {
        // 기업이 작성한 공고 찾기
        studyService.findStudyFromCompany(studyId, companyDetails.getCompany());
        // 해당 공고의 신청서 찾기
        Application application  = applicationRepository.findById(applicationId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
        );

        String applicationViewStatusEnum = ApplicationViewStatusEnum.VIEWED.toString();
        application.viewStatusUpdate(applicationViewStatusEnum);

        ApplicationFromStudyResponseDto dto = new ApplicationFromStudyResponseDto(application.getUser(), application.getUser().getUserinfo(), application);
        return dto;
    }

    // 신청서 승인, 거절
    @Transactional
    public void applicationStatus(CompanyDetailsImpl companyDetails, ApplicationStatusRequestDto statusRequestDto, Long studyId, Long applicationId) {
        if (companyDetails != null) {
            // 공고, 신청서 찾기
            Application application = applicationRepository.findByIdAndStudyId(applicationId,studyId).orElseThrow(
                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

            if(statusRequestDto.getApprovalStatus().equals("승인")) {
                application.statusUpdate(ApprovalStatusEnum.APPROVED.toString());
            } else if(statusRequestDto.getApprovalStatus().equals("거절")) {
                application.statusUpdate(ApprovalStatusEnum.REJECTED.toString());
            }
        } else {
            // 인증된 회사 정보가 없는 경우, 예외 처리
            throw new ApplicationException(ApplicationErrorCode.NOT_HAVE_PERMISSION);
        }
    }

    // 공고별 전체 신청서 확인
    @Transactional
    public List<ApplicationFromStudyResponseDto> applicationFromStudy(CompanyDetailsImpl companyDetails, Long studyId) {
        // 1. 기업이 작성한 공고의id를 사용해 새로운 공고에 저장. -> 로그인 기업이 작성한 공고
        Study study = studyService.findStudyFromCompany(studyId, companyDetails.getCompany());

        List<ApplicationFromStudyResponseDto> applicationDtos = new ArrayList<>();
        // 2. 1번의 공고id를 사용해 해당 공고의 신청서를 리스트에 저장.
        List<Application> applications = applicationRepository.findByStudyId(study.getId());

        for (Application application : applications) {
            applicationDtos.add(new ApplicationFromStudyResponseDto(application.getUser(), application.getUser().getUserinfo(), application));
        }

        return applicationDtos;
    }
}
