package com.example.lablink.study.service;

import com.example.lablink.S3Image.dto.S3ResponseDto;
import com.example.lablink.S3Image.entity.S3Image;
import com.example.lablink.S3Image.service.S3Service;
import com.example.lablink.S3Image.service.S3UploaderService;
import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.dto.responseDto.StudyDetailResponseDto;
import com.example.lablink.study.dto.responseDto.StudyResponseDto;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.repository.StudyRepository;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final GetStudyService getStudyService;
    private final BookmarkService bookmarkService;
    private final S3UploaderService s3UploaderService;
    private final S3Service s3Service;

    // 게시글 작성
    @Transactional
    public void createStudy(StudyRequestDto requestDto, CompanyDetailsImpl companyDetails, S3ResponseDto s3ResponseDto) {
        Company company = isCompanyLogin(companyDetails);
        StudyStatusEnum status = setStatus(requestDto);
        Study study;
        if (s3ResponseDto != null) {
            String storedFileName = s3ResponseDto.getUploadFileUrl();
            study = new Study(requestDto, status, company, storedFileName);
        } else {
            study = new Study(requestDto, status, company, null);
        }
        studyRepository.save(study);
    }

    // 게시글 조회 (전체 조회 및 검색 조회 등)
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getStudies(StudySearchOption searchOption, Integer pageIndex, Integer pageCount, UserDetailsImpl userDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();
        for (Study study : studyRepository.findAllByOrderByEndDateDesc()){
            // 북마크 기능 추가
            boolean isBookmarked = bookmarkService.checkBookmark(study.getId(), user);
            studyResponseDtos.add(new StudyResponseDto(study, isBookmarked));
        }
        // TODO: 검색 관련 코드 추가
        return studyResponseDtos;
    }


    // 인기 공고로 변경 (지원자순)
    public List<StudyResponseDto> getSortedStudies(String sortedType, UserDetailsImpl userDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        List<Study> studies = new ArrayList<>();
        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();

        // TODO: 추천하는 실험 목록 뽑아내는 코드 작성

        if (sortedType == null){
            // TODO: 지원자 순으로 정렬
        }
        if (Objects.equals(sortedType, "latest")){
            studies = studyRepository.findAllByOrderByCreatedAtDesc();
        }

        if (Objects.equals(sortedType, "pay")){
            studies = studyRepository.findAllByOrderByPayDesc();
        }

        for (Study study : studies){
            // 북마크 기능 추가
            boolean isbookmarked = bookmarkService.checkBookmark(study.getId(), user);
            studyResponseDtos.add(new StudyResponseDto(study, isbookmarked));
        }
        return studyResponseDtos;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public StudyDetailResponseDto getDetailStudy(Long studyId, UserDetailsImpl userDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        Study study = getStudyService.getStudy(studyId);
        boolean isbookmarked = bookmarkService.checkBookmark(study.getId(), user);
        return new StudyDetailResponseDto(study, isbookmarked);
    }

    // 게시글 수정
    // TODO : 이미지 수정 넣기
    @Transactional
    public void updateStudy(Long studyId, StudyRequestDto requestDto, CompanyDetailsImpl companyDetails) {
        Company company = isCompanyLogin(companyDetails);
        Study study = getStudyService.getStudy(studyId);
        StudyStatusEnum status = setStatus(requestDto);
        // 만약 수정하기 전 공고에 image가 있었다면 이미지 가져오기
        S3Image s3Image = s3Service.getS3Image(study.getImageURL());
        checkRole(studyId, company);
        MultipartFile image = requestDto.getImage();
        if(image != null){
            if(s3Image != null){
                s3UploaderService.deleteFile(s3Image.getId());
            }
            S3ResponseDto s3ResponseDto = s3UploaderService.uploadFiles("thumbnail", image);
            study.update(requestDto, status, s3ResponseDto.getUploadFileUrl());
        } else{
            study.update(requestDto, status, null);
        }
    }

    private StudyStatusEnum setStatus(StudyRequestDto requestDto){
        StudyStatusEnum status;
        if (requestDto.getEndDate().isBefore(LocalDateTime.now())) {
            status = StudyStatusEnum.CLOSED;
        } else{
            status = StudyStatusEnum.ONGOING;
        }
        return status;
    }

    // 게시글 삭제
    // TODO : 이미지 삭제 넣기
    @Transactional
    public void deleteStudy(Long studyId, CompanyDetailsImpl companyDetails) {
        // companyDetails == null 아면 로그인이 필요한 서비스입니다. 날려주기
        Company company = isCompanyLogin(companyDetails);

        // image가 있는 공고였다면 s3에서 지워줘야 함
        Study study = getStudyService.getStudy(studyId);
        S3Image s3Image = s3Service.getS3Image(study.getImageURL());
        s3UploaderService.deleteFile(s3Image.getId());

        checkRole(studyId, company);
        studyRepository.deleteById(studyId);
    }

    // companyDetails == null 아면 로그인이 필요한 서비스입니다. 날려주기
    private Company isCompanyLogin(CompanyDetailsImpl companyDetails){
        if (companyDetails != null){
            return companyDetails.getCompany();
        } else{
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
    }


    // checkRole 게시글 권한 확인 (해당 게시글을 작성자와 똑같은지 확인)
    private void checkRole(Long studyId, Company company){
        studyRepository.findByIdAndCompany(studyId, company).orElseThrow(
                () -> new StudyException(StudyErrorCode.NOT_AUTHOR)
        );
    }
}
