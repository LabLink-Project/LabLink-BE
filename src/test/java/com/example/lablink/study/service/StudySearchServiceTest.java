package com.example.lablink.study.service;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.responseDto.LatestSearchKeyword;
import com.example.lablink.study.dto.responseDto.StudyResponseDto;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.repository.StudyRepository;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.security.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

import static com.example.lablink.support.StudyStub.Study1;
import static com.example.lablink.support.StudyStub.Study2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySearchServiceTest {
    @InjectMocks
    private StudySearchService studySearchService;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private BookmarkService bookmarkService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Test
    @DisplayName("키워드 검색 조회")
    void testGetStudiesWithKeyword() {
        // given
        StudySearchOption searchOption = new StudySearchOption();
        searchOption.setKeyword("keyword");
        Integer pageIndex = 0;
        Integer pageCount = 10;
        String sortedType = null;
        User user = new User();
        user.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
        Study study1 = Study1.of();
        Study study2 = Study2.of();
        List<Study> studies = new ArrayList<>();
        studies.add(study1);
        studies.add(study2);
        given(studyRepository.searchStudiesBySearchOption(any(StudySearchOption.class), anyInt(), anyInt())).willReturn(studies);

        ZSetOperations<String, String> zSetOperations = mock(ZSetOperations.class);
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.add(anyString(), anyString(), anyDouble())).willReturn(null);
        given(zSetOperations.incrementScore(anyString(), anyString(), anyDouble())).willReturn(null);

        // When
        List<StudyResponseDto> studyResponseDtos = studySearchService.getStudies(searchOption, pageIndex, pageCount, sortedType, userDetails);

        // Then
        verify(studyRepository).searchStudiesBySearchOption(eq(searchOption), eq(pageIndex), eq(pageCount));
        verify(redisTemplate.opsForZSet()).add(eq("1"), eq("keyword"), anyDouble());
        verify(redisTemplate.opsForZSet()).incrementScore(eq("ranking"), eq("keyword"), eq(1.0));
        assertEquals(2, studyResponseDtos.size());
    }

    // todo : redis test

    @Test
    @DisplayName("일반 전체 조회")
    public void getStudies(){
        // given
        StudySearchOption searchOption = mock(StudySearchOption.class);
        given(searchOption.hasValue()).willReturn(false);
        Integer pageIndex = 0;
        Integer pageCount = 10;
        String sortedType = null;
        User user = new User();
        user.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
        Study study1 = Study1.of();
        Study study2 = Study2.of();
        List<Study> studies = new ArrayList<>();
        studies.add(study1);
        studies.add(study2);
        given(studyRepository.findAllByOrderByEndDateDesc()).willReturn(studies);

        // when
        List<StudyResponseDto> studyResponseDtos = studySearchService.getStudies(searchOption, pageIndex, pageCount, sortedType, userDetails);

        // then
        verify(studyRepository).findAllByOrderByEndDateDesc();
        assertEquals(2, studyResponseDtos.size());
    }

    @Test
    @DisplayName("정렬 조회")
    public void getSortedStudies(){
        // given
        StudySearchOption searchOption = mock(StudySearchOption.class);
        given(searchOption.hasValue()).willReturn(false);
        Integer pageIndex = 0;
        Integer pageCount = 10;
        String sortedType = "pay";
        User user = new User();
        user.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
        Study study1 = Study1.of();
        Study study2 = Study2.of();
        List<Study> studies = new ArrayList<>();
        studies.add(study1);
        studies.add(study2);
        given(studySearchService.getSortedStudies(sortedType)).willReturn(studies);

        // when
        List<StudyResponseDto> studyResponseDtos = studySearchService.getStudies(searchOption, pageIndex, pageCount, sortedType, userDetails);

        // then
        assertEquals(2, studyResponseDtos.size());
    }

    @Test
    @DisplayName("최근 검색 조회")
    void testLatestSearchKeyword() {
        // given
        User user = new User();
        user.setId(1L);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUser()).willReturn(user);

        String key = Long.toString(user.getId());
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new LinkedHashSet<>();
        typedTuples.add(mock(ZSetOperations.TypedTuple.class));
        typedTuples.add(mock(ZSetOperations.TypedTuple.class));
        typedTuples.add(mock(ZSetOperations.TypedTuple.class));
        given(zSetOperations.reverseRangeWithScores(key, 0, 9)).willReturn(typedTuples);
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        List<LatestSearchKeyword> latestSearchKeywords = studySearchService.latestSearchKeyword(userDetails);

        // then
        Assertions.assertNotNull(latestSearchKeywords);
    }

    @Test
    public void latestSearchKeyword_NotLoginUser(){
        // given
        UserDetailsImpl userDetails = null;

        // when
        List<LatestSearchKeyword> latestSearchKeywords = studySearchService.latestSearchKeyword(userDetails);

        // then
        Assertions.assertNull(latestSearchKeywords);
    }

    @Test
    @DisplayName("최근 검색 기록 삭제")
    void testDeleteSearchKeywordWithLogin() {
        // given
        User user = new User();
        user.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());

        String key = Long.toString(user.getId());
        String deleteWord = "test";
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        studySearchService.deleteSearchKeyword(userDetails, deleteWord);

        // then
        verify(zSetOperations).remove(key, deleteWord);
    }

    @Test
    @DisplayName("최근 검색 기록 삭제 - 로그인하지 않은 경우, StudyException이 발생")
    void testDeleteSearchKeywordWithoutLogin() {
        // given
        UserDetailsImpl userDetails = null;
        String deleteWord = "test";

        // when & then
        assertThrows(StudyException.class, () -> studySearchService.deleteSearchKeyword(userDetails, deleteWord));
    }

    @Test
    @DisplayName("인기검색어 리스트 조회")
    void testSearchRankList() {
        // given
        String key = "ranking";
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(mock(ZSetOperations.TypedTuple.class));

        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRangeWithScores(key, 0, 9)).willReturn(typedTuples);

        // when & then
        Assertions.assertDoesNotThrow(() -> {studySearchService.searchRankList();});
        assertEquals(typedTuples.size(), studySearchService.searchRankList().size());
    }

    @Test
    @DisplayName("공고 정렬 조회 - 인기순")
    void testGetSortedStudies_popularity() {
        List<Study> studies = new ArrayList<>();
        studies.add(mock(Study.class));
        studies.add(mock(Study.class));

        studySearchService = new StudySearchService(studyRepository, null, null);

        when(studyRepository.findAllByOrderByCurrentApplicantCountDesc()).thenReturn(studies);

        Assertions.assertDoesNotThrow(() -> {studySearchService.getSortedStudies("popularity");});
        Assertions.assertEquals(studies.size(), studySearchService.getSortedStudies("popularity").size());
    }

    @Test
    @DisplayName("공고 정렬 조회 - 최신순")
    public void testGetSortedStudies_latest() {
        List<Study> studies = new ArrayList<>();
        studies.add(mock(Study.class));
        studies.add(mock(Study.class));

        studySearchService = new StudySearchService(studyRepository, null, null);

        when(studyRepository.findAllByOrderByCreatedAtDesc()).thenReturn(studies);

        Assertions.assertDoesNotThrow(() -> {
            studySearchService.getSortedStudies("latest");
        });

        Assertions.assertEquals(studies.size(), studySearchService.getSortedStudies("latest").size());
    }

    @Test
    @DisplayName("공고 정렬 조회 - 급여순")
    public void testGetSortedStudies_pay() {
        List<Study> studies = new ArrayList<>();
        studies.add(mock(Study.class));
        studies.add(mock(Study.class));

        studySearchService = new StudySearchService(studyRepository, null, null);

        when(studyRepository.findAllByOrderByPayDesc()).thenReturn(studies);

        Assertions.assertDoesNotThrow(() -> {
            studySearchService.getSortedStudies("pay");
        });

        Assertions.assertEquals(studies.size(), studySearchService.getSortedStudies("pay").size());
    }
}