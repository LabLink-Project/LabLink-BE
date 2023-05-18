package com.example.lablink.global.scheduler;

import com.example.lablink.domain.study.dto.responseDto.SearchRankResponseDto;
import com.example.lablink.domain.study.entity.SearchRankKeyword;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.entity.StudyStatusEnum;
import com.example.lablink.domain.study.repository.SearchRankKeywordRepository;
import com.example.lablink.domain.study.repository.StudyRepository;

import com.example.lablink.domain.study.service.StudySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final StudyRepository studyRepository;
    private final StudySearchService studySearchService;
    private final SearchRankKeywordRepository searchRankKeywordRepository;
//    private final StudyMapper studyMapper;
    /*private final JavaMailSender emailSender;
        private final FeedBackService feedBackService;*/
    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 0 * * *")
    // todo : exception ?
    public void updateStatus() throws InterruptedException {
        log.info("studystatus 업데이트 실행");
        // findAll이 아니라 enddate가 LocalDateTime보다 이후면 들고오기 (쿼리짜서)
        List<Study> studies = studyRepository.getAllByEndDate();
        for (Study study : studies) {
            // endDate와 localDate 비교 및 studystatus 변경
            LocalDateTime currentDateTime = LocalDateTime.now();
            // endDate가 localDate보다 이전인 경우
            if (study.getEndDate().isBefore(currentDateTime)) {
                study.updateStatus(StudyStatusEnum.CLOSED);
            }
        }
    }

    // 매일 자정 인기 검색어 rds로 저장
    @Scheduled(cron = "0 0 0 * * *")
    public void saveKeyword() {
        log.info("매일 자정 인기 검색어 rds로 저장");
        List<SearchRankResponseDto> searchRanks = studySearchService.searchRankList();
        // searchrank entity 만들어서 저장
        List<SearchRankKeyword> searchs = new ArrayList<>();
        for (SearchRankResponseDto searchRank : searchRanks) {
            SearchRankKeyword search = new SearchRankKeyword();
            search.setRankKeyword(searchRank.getRankKeyword());
            search.setScore(searchRank.getScore());
            searchs.add(search);
        }
        searchRankKeywordRepository.saveAll(searchs);
    }
}
