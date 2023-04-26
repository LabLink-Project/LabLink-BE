package com.example.lablink.study.service;

import com.example.lablink.S3Image.dto.S3ResponseDto;
import com.example.lablink.S3Image.entity.S3Image;
import com.example.lablink.S3Image.service.S3Service;
import com.example.lablink.S3Image.service.S3UploaderService;
import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.dto.responseDto.StudyDetailResponseDto;
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
import java.util.List;

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
    public void createStudy(StudyRequestDto requestDto, CompanyDetailsImpl companyDetails, S3ResponseDto thumbnailS3ResponseDto, S3ResponseDto detailS3ResponseDto) {
        // todo : isCompanyLogin 통일
        Company company = isCompanyLogin(companyDetails);
        StudyStatusEnum status = setStatus(requestDto.getEndDate());
        Study study;
        String thumbnailImageURL = null;
        String detailImageURL = null;
        if (thumbnailS3ResponseDto != null) {
            thumbnailImageURL = thumbnailS3ResponseDto.getUploadFileUrl();
        }
        if (detailS3ResponseDto != null){
            detailImageURL = detailS3ResponseDto.getUploadFileUrl();
        }
        study = new Study(requestDto, status, company, thumbnailImageURL, detailImageURL);
        studyRepository.save(study);
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public StudyDetailResponseDto getDetailStudy(Long studyId, UserDetailsImpl userDetails) {
        // todo : userDetails 통일
        User user = userDetails == null ? null : userDetails.getUser();
        Study study = getStudyService.getStudy(studyId);
        boolean isbookmarked = bookmarkService.checkBookmark(study.getId(), user);
        return new StudyDetailResponseDto(study, isbookmarked);
    }

    // 게시글 수정
    // 이미지 수정 refactoring
    // 수정 requestDto가 따로 있어야 하나 ? or 수정하기 눌렀을 때 내용 보여주기 -> patch
    @Transactional
    public void updateStudy(Long studyId, StudyRequestDto requestDto, CompanyDetailsImpl companyDetails) {
        Company company = isCompanyLogin(companyDetails);
        Study study = getStudyService.getStudy(studyId);
        checkRole(studyId, company);

//        ModelMapper modelMapper = new ModelMapper();
//
//        PropertyMap<StudyRequestDto, Study> propertyMap = new PropertyMap<StudyRequestDto, Study>() {
//            @Override
//            protected void configure() {
//                skip().setStatus(null);
//                skip().setThumbnailImageURL(null);
//                skip().setDetailImageURL(null);
//            }
//        };
//
//        modelMapper.addMappings(propertyMap);
//
//        study = modelMapper.map(requestDto, Study.class);

        StudyStatusEnum status = study.getStatus();
        if(requestDto.getEndDate() != null) status = setStatus(requestDto.getEndDate());

        // 만약 수정하기 전 공고에 image가 있었다면 이미지 가져오기
        S3Image thumbnailS3Image = s3Service.getS3Image(study.getThumbnailImageURL());
        S3Image detailS3Image = s3Service.getS3Image(study.getDetailImageURL());
        MultipartFile thumbnailImage = requestDto.getThumbnailImage();
        MultipartFile detailImage = requestDto.getDetailImage();
        String thumbnailImageURL = null;
        String detailImageURL = null;
        // thumbnailImage
        if(thumbnailImage != null){
            if(thumbnailS3Image != null){
                s3UploaderService.deleteFile(thumbnailS3Image.getId());
            }
            S3ResponseDto thumbnailS3ResponseDto = s3UploaderService.uploadFiles("thumbnail", thumbnailImage);
            thumbnailImageURL = thumbnailS3ResponseDto.getUploadFileUrl();
        }
        // detailImage
        if(detailImage != null){
            if(detailS3Image != null){
                s3UploaderService.deleteFile(detailS3Image.getId());
            }
            S3ResponseDto detailS3ResponseDto = s3UploaderService.uploadFiles("detail", detailImage);
            detailImageURL = detailS3ResponseDto.getUploadFileUrl();
        }

        study.update(status, thumbnailImageURL, detailImageURL);
    }

    public StudyStatusEnum setStatus(LocalDateTime endDate){
        StudyStatusEnum status;
        if (endDate.isBefore(LocalDateTime.now())) {
            status = StudyStatusEnum.CLOSED;
        } else{
            status = StudyStatusEnum.ONGOING;
        }
        return status;
    }

    // 기본 이미지로 변경 (thumbnail)
    // xxx : deleteThumbnail(), deleteDetailImage() 2개로 하는게 맞을까 ?
    @Transactional
    public void deleteThumbnail(Long studyId, CompanyDetailsImpl companyDetails) {
        Company company = isCompanyLogin(companyDetails);
        Study study = getStudyService.getStudy(studyId);
        checkRole(studyId, company);
        S3Image thumbnailS3Image = s3Service.getS3Image(study.getThumbnailImageURL());
        if(thumbnailS3Image != null){
            s3UploaderService.deleteFile(thumbnailS3Image.getId());
        }
        // study update 해주깅
        study.deleteThumbnail();
    }

    // 기본 이미지로 변경 (detailImage)
    @Transactional
    public void deleteDetailImage(Long studyId, CompanyDetailsImpl companyDetails) {
        Company company = isCompanyLogin(companyDetails);
        Study study = getStudyService.getStudy(studyId);
        checkRole(studyId, company);
        S3Image detailS3Image = s3Service.getS3Image(study.getDetailImageURL());
        if(detailS3Image != null){
            s3UploaderService.deleteFile(detailS3Image.getId());
        }
        study.deleteDetailImage();
    }

    // 게시글 삭제
    // 이미지 삭제 refactoring
    @Transactional
    public void deleteStudy(Long studyId, CompanyDetailsImpl companyDetails) {
        // companyDetails == null 아면 로그인이 필요한 서비스입니다. 날려주기
        Company company = isCompanyLogin(companyDetails);

        // 유저의 북마크도 삭제해줘야 한다 ??
        // -> 삭제된 공고입니다를 달아주느게 더 나을 것 같은데
        // xxx : 북마크에서 지워버리는게 아니라 '삭제된 공고입니다'로 리팩토링 해주기
        bookmarkService.deleteByStudyId(studyId);

        // image가 있는 공고였다면 s3에서 지워줘야 함
        Study study = getStudyService.getStudy(studyId);
        S3Image thumbnailS3Image = s3Service.getS3Image(study.getThumbnailImageURL());
        S3Image DetailS3Image = s3Service.getS3Image(study.getDetailImageURL());
        if(thumbnailS3Image != null){
            s3UploaderService.deleteFile(thumbnailS3Image.getId());
        }
        if(DetailS3Image != null){
            s3UploaderService.deleteFile(DetailS3Image.getId());
        }
        checkRole(studyId, company);
        studyRepository.deleteById(studyId);
    }

    // companyDetails == null 아면 로그인이 필요한 서비스입니다. 날려주기
    // xxx: isCompanyLogin() testcode짜려면 public으로 바꿔야하는데
    //  1. public으로 바꾸고 테스트코드 짜는게 좋을까
    //  2. private으로 하고 테스트코드 안짜는게 나을까?
    public Company isCompanyLogin(CompanyDetailsImpl companyDetails){
        if (companyDetails != null){
            return companyDetails.getCompany();
        } else{
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
    }


    // checkRole 게시글 권한 확인 (해당 게시글을 작성자와 똑같은지 확인)
    public void checkRole(Long studyId, Company company){
        studyRepository.findByIdAndCompany(studyId, company).orElseThrow(
                () -> new StudyException(StudyErrorCode.NOT_AUTHOR)
        );
    }

    // 기업별 공고 찾기
    public List<Study> findAllCompanyStudy(Company company) {
        if (company == null) {
            throw new StudyException(StudyErrorCode.STUDY_NOT_FOUND);
        }
        return studyRepository.findAllByCompany(company);
    }

    // todo : checkRole 이랑 동일
    // 기업이 작성한 공고 찾기
    public Study findStudyFromCompany(Long studyId, Company company) {
        return studyRepository.findByIdAndCompany(studyId, company)
            .orElseThrow(() -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND));
    }
}
