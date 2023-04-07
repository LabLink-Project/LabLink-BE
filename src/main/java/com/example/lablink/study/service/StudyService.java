//package com.example.lablink.study.service;
//
//import com.example.lablink.category.entity.Category;
//import com.example.lablink.category.service.CategoryService;
//import com.example.lablink.company.entity.Company;
//import com.example.lablink.study.dto.StudySearchOption;
//import com.example.lablink.study.dto.requestDto.StudyRequestDto;
//import com.example.lablink.study.dto.responseDto.StudyDetailResponseDto;
//import com.example.lablink.study.dto.responseDto.StudyResponseDto;
//import com.example.lablink.study.entity.Study;
//import com.example.lablink.study.exception.StudyErrorCode;
//import com.example.lablink.study.exception.StudyException;
//import com.example.lablink.study.repository.StudyRepository;
//import com.example.lablink.user.entity.User;
//import com.example.lablink.user.security.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class StudyService {
//    private final CategoryService categoryService;
//    private final StudyRepository studyRepository;
//
//    // 게시글 작성
//    @Transactional
//    public void createStudy(StudyRequestDto requestDto, Company company) {
//        Study study = studyRepository.save(new Study(requestDto, company));
//        categoryService.saveCategory(requestDto, study);
//    }
//
//    // 게시글 조회 (전체 조회 및 검색 조회 등)
//    @Transactional(readOnly = true)
//    public List<StudyResponseDto> getStudies(StudySearchOption searchOption, Integer pageIndex, Integer pageCount, UserDetailsImpl userDetails) {
//        User user = userDetails == null ? null : userDetails.getUser();
//        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();
//
//        // TODO: 검색 관련 코드 추가
//        // TODO: 북마크 관련 코드 추가
//
//        return studyResponseDtos;
//    }
//
//    public List<StudyResponseDto> getRecommendStudies(User user) {
//        // XXX : user가 선택한 지역과 북마크한 목록의 카테고리를 보고 추천해주는 건가 ? 정확히 알아보자
//        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();
//
//        // TODO: 추천하는 실험 목록 뽑아내는 코드 작성
//
//        return studyResponseDtos;
//    }
//
//    // 게시글 상세 조회
//    @Transactional(readOnly = true)
//    public StudyDetailResponseDto getDetailStudy(Long id, UserDetailsImpl userDetails) {
//        User user = userDetails == null ? null : userDetails.getUser();
//        // TODO: user 확인 후 북마크 기능 연동
//        Study study = getStudy(id);
//        Category category = categoryService.getCategory(study);
//        return new StudyDetailResponseDto(study, category);
//    }
//
//    // getStudy 매서드
//    @Transactional(readOnly = true)
//    public Study getStudy(Long id){
//        return studyRepository.findById(id).orElseThrow(
//                ()-> new StudyException(StudyErrorCode.STUDY_NOT_FOUND)
//        );
//    }
//
//    // 게시글 수정
//
//    // 게시글 삭제
//}
