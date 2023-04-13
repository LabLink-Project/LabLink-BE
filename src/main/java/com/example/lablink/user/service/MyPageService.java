//package com.example.lablink.user.service;
//
//import com.example.lablink.application.entity.Application;
//
//import com.example.lablink.application.entity.ApplicationViewStatusEnum;
//import com.example.lablink.application.exception.ApplicationErrorCode;
//import com.example.lablink.application.exception.ApplicationException;
//import com.example.lablink.application.service.ApplicationService;
//import com.example.lablink.company.entity.Company;
//import com.example.lablink.company.security.CompanyDetailsImpl;
//import com.example.lablink.study.entity.Study;
//import com.example.lablink.study.service.StudyService;
//import com.example.lablink.user.dto.response.MyLabResponseDto;
//import com.example.lablink.user.security.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MyPageService {
//
//    private final ApplicationService applicationService;
//    private final StudyService studyService;
//
//    // 마이페이지 신청서 조회
//    @Transactional
//    public List<MyLabResponseDto> getMyLabs(UserDetailsImpl userDetails, Study study, Application application) {
//        if (userDetails != null) {
////            Application application = applicationRepository.findByIdAndUserEmail(applicationId, userDetails.getUser().getEmail()).orElseThrow(
////                () -> new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
////            );
//            List<MyLabResponseDto> myLabs = new ArrayList<>();
//            List<Application> applications = applicationService.findAllMyApplications(application.getId(), userDetails.getUser());
//            System.out.println("______________" + 1);
//            System.out.println(applications.isEmpty());
//            for (Application application1 : applications) {
//                System.out.println("______________" + 2);
//                if (!application.getStudyId().equals(study.getId()) || !application.getUser().getId().equals(userDetails.getUser().getId())) {
//                    System.out.println("______________" + 3);
//                    throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
//                }
//                System.out.println("______________" + 4);
//                myLabs.add(new MyLabResponseDto(study, application1));
//                System.out.println("______________" + 5);
//            }
//            System.out.println("______________" + 6);
//            return myLabs;
//        } else {
//            throw new ApplicationException(ApplicationErrorCode.NOT_HAVE_PERMISSION);
//        }
//    }
//
////    @Transactional
////    public Application getApplication(UserDetailsImpl userDetails, CompanyDetailsImpl companyDetails, Long studyId, Long applicationId) {
////        if (userDetails != null) {
////            Application application = applicationRepository.findByIdAndUserEmail(applicationId, userDetails.getUser().getEmail()).orElseThrow(
////                () -> new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
////            );
////
////            if (!application.getStudyId().equals(studyId) || !application.getUser().getId().equals(userDetails.getUser().getId())) {
////                throw new ApplicationException(ApplicationErrorCode.NOT_AUTHOR);
////            }
////            return application;
////        }
////        if (companyDetails != null) {
////            Application application = applicationRepository.findById(applicationId).orElseThrow(
////                ()->new ApplicationException(ApplicationErrorCode.APPLICATION_NOT_FOUND)
////            );
////
////            ApplicationViewStatusEnum applicationViewStatusEnum = ApplicationViewStatusEnum.VIEWED;
////            application.viewStatusUpdate(applicationViewStatusEnum);
////            return application;
////        } else {
////            throw new ApplicationException(ApplicationErrorCode.NOT_HAVE_PERMISSION);
////        }
////    }
//}
