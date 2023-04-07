package com.example.lablink.study.service;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.category.entity.Category;
import com.example.lablink.category.service.CategoryService;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.dto.responseDto.StudyDetailResponseDto;
import com.example.lablink.study.dto.responseDto.StudyResponseDto;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.repository.StudyRepository;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final CategoryService categoryService;
    private final StudyRepository studyRepository;
    private final GetStudyService getStudyService;
    private final BookmarkService bookmarkService;

    // 게시글 작성
    @Transactional
    public void createStudy(StudyRequestDto requestDto, CompanyDetailsImpl companyDetails) {
        Study study = studyRepository.save(new Study(requestDto, companyDetails.getCompany()));
        categoryService.saveCategory(requestDto, study.getId());
    }

    // 게시글 작성
    // xxx: test
//    @Transactional
//    public void createStudy(StudyRequestDto requestDto) {
//        Study study = studyRepository.save(new Study(requestDto));
//        categoryService.saveCategory(requestDto, study.getId());
//    }

    // 게시글 조회 (전체 조회 및 검색 조회 등)
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getStudies(StudySearchOption searchOption, Integer pageIndex, Integer pageCount, UserDetailsImpl userDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();
        for (Study study : studyRepository.findAllByOrderByEndDateDesc()){
            // 카테고리 추가
            Category category = categoryService.getCategory(study.getId());
            // 북마크 기능 추가
            boolean isBookmarked = bookmarkService.checkBookmark(study.getId(), user);
            studyResponseDtos.add(new StudyResponseDto(study, category, isBookmarked));
        }
        // TODO: 검색 관련 코드 추가
        return studyResponseDtos;
    }


    // 인기 공고로 변경 (지원자순)
    public List<StudyResponseDto> getSortedStudies(String sortedType, UserDetailsImpl userDetails) {
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
            // 카테고리 추가
            Category category = categoryService.getCategory(study.getId());
            // 북마크 기능 추가
            boolean isBookmarked = bookmarkService.checkBookmark(study.getId(), userDetails.getUser());
            studyResponseDtos.add(new StudyResponseDto(study, category, isBookmarked));
        }
        return studyResponseDtos;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public StudyDetailResponseDto getDetailStudy(Long studyId, UserDetailsImpl userDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        Study study = getStudyService.getStudy(studyId);
        Category category = categoryService.getCategory(study.getId());
        boolean isBookmarked = bookmarkService.checkBookmark(study.getId(), user);
        return new StudyDetailResponseDto(study, category, isBookmarked);
    }

    // 게시글 수정
    public void updateStudy(Long studyId, StudyRequestDto requestDto, CompanyDetailsImpl companyDetails) {
        Study study = getStudyService.getStudy(studyId);
        checkRole(studyId, companyDetails.getCompany());
        study.update(requestDto);
    }

    // 게시글 삭제
    public void deleteStudy(Long studyId, CompanyDetailsImpl companyDetails) {
        checkRole(studyId, companyDetails.getCompany());
        studyRepository.deleteById(studyId);
    }


    // checkRole 게시글 권한 확인 (해당 게시글을 작성자와 똑같은지 확인)
    private void checkRole(Long studyId, Company company){
        studyRepository.findByIdAndCompany(studyId, company).orElseThrow(
                () -> new StudyException(StudyErrorCode.NOT_AUTHOR)
        );
    }
}
