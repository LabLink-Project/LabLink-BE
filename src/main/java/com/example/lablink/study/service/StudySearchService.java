package com.example.lablink.study.service;

import com.example.lablink.application.service.ApplicationService;
import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.responseDto.StudyResponseDto;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.repository.StudyRepository;
import com.example.lablink.study.repository.StudySearchQueryRepository;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class StudySearchService {

    private final StudyRepository studyRepository;
    private final StudySearchQueryRepository studySearchQueryRepository;
    private final BookmarkService bookmarkService;
    private final RedisTemplate<String, String> redisTemplate;

    // 게시글 조회 (전체 조회 및 검색 조회 등)
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getStudies(StudySearchOption searchOption, String keyword, Integer pageIndex, Integer pageCount, String sortedType, UserDetailsImpl userDetails, CompanyDetailsImpl companyDetails) {
        User user = userDetails == null ? null : userDetails.getUser();
        Company company = companyDetails == null ? null : companyDetails.getCompany();
        List<Study> studies = new ArrayList<>();
        List<StudyResponseDto> studyResponseDtos = new ArrayList<>();
        // 정렬 조건이 들어온다면
        if(sortedType != null){
            studies = getSortedStudies(sortedType);
        }

        // 상세 검색
        if(searchOption.hasValue()){
//            studies = studyRepository.searchStudiesBySearchOption(searchOption, pageIndex, pageCount);
            studies = studySearchQueryRepository.searchStudies(searchOption, pageIndex, pageCount);
        }

        if(keyword != null){
            studies = studySearchQueryRepository.searchStudiesByKeyword(keyword, pageIndex, pageCount);
//            if(searchOption.getKeyword() != null){
//                if(user != null){
//                    // 최신검색어 구현
//                    Double timestamp = (double) System.currentTimeMillis();
//                    redisTemplate.opsForZSet().add(user.getId().toString(),  searchOption.getKeyword(), timestamp);
//                }
//                // 인기검색어 구현
//                Double score = 0.0;
//                try {
//                    // 검색을하면 해당검색어를 value에 저장하고, score를 1 준다
//                    redisTemplate.opsForZSet().incrementScore("ranking", searchOption.getKeyword(), 1);
//                } catch (Exception e) {
//                    System.out.println(e.toString());
//                }
//                //score를 1씩 올려준다.
////                redisTemplate.opsForZSet().incrementScore("ranking", searchOption.getKeyword(), score);
//            }
        }

        // 일반 전체 조회
        if(sortedType == null && !searchOption.hasValue()){
            studies = studyRepository.findAllByOrderByEndDateDesc();
//            studies = studyRepository.findAll();
        }

        for (Study study : studies){
            boolean isBookmarked = false;
            boolean isApplied = false;
            if(user != null){
                // 북마크 기능 추가
                isBookmarked = bookmarkService.checkBookmark(study.getId(), user);
                /*// 지원 현황 추가
                isApplied = applicationService.checkApplication(study.getId(), user);*/
            }
            if(company != null){
                isBookmarked = bookmarkService.checkBookmark(study.getId(), company);
            }
            studyResponseDtos.add(new StudyResponseDto(study, isBookmarked/*, isApplied*/));
        }

        return studyResponseDtos;
    }
    // 최근 검색 조회 (유저Id == key)
    // todo : 몇개까지 저장 ?
    /*public List<LatestSearchKeyword> latestSearchKeyword(UserDetailsImpl userDetails){
        if(userDetails != null){
            User user = userDetails.getUser();
            String key = user.getId().toString();
            ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);
            return typedTuples.stream().map(LatestSearchKeyword::convertToLatestSearchKeyword).collect(Collectors.toList());
        } else{
            return null;
        }
    }

    public void deleteSearchKeyword(UserDetailsImpl userDetails, String deleteWord) {
        if(userDetails != null){
            User user = userDetails.getUser();
            String key = user.getId().toString();
            redisTemplate.opsForZSet().remove(key, deleteWord);
        } else {
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
    }

    // 인기검색어 리스트 1위~10위까지
    public List<SearchRankResponseDto> searchRankList() {
        String key = "ranking";
        // ZSetOperations 객체 생성
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        // score순으로 10개 보여줌
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);
        return typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }*/

    @Transactional
    // 공고 정렬 조회
    public List<Study> getSortedStudies(String sortedType) {
        List<Study> studies = new ArrayList<>();
        // 인기순 == 지원자 많은 순
        if (Objects.equals(sortedType, "popularity")){
            // 지원자 많은 순으로 정렬
            studies = studyRepository.findAllByOrderByCurrentApplicantCountDesc();
        }
        // 최신순
        if (Objects.equals(sortedType, "latest")){
            studies = studyRepository.findAllByOrderByCreatedAtDesc();
        }
        // 단가 높은 순
        if (Objects.equals(sortedType, "pay")){
            studies = studyRepository.findAllByOrderByPayDesc();
        }
        return studies;
    }
}
