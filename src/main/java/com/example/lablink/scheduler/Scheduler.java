package com.example.lablink.scheduler;

import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import com.example.lablink.study.repository.StudyRepository;
import com.example.lablink.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final StudyRepository studyRepository;
    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatus() throws InterruptedException {
        log.info("studystatus 업데이트 실행");
        List<Study> studies = studyRepository.findAll();
        for (Study study : studies) {
            // endDate와 localDate 비교 및 studystatus 변경
            LocalDateTime currentDateTime = LocalDateTime.now();
            // endDate가 localDate보다 이전인 경우
            if (study.getEndDate().isBefore(currentDateTime)) {
                study.updateStatus(StudyStatusEnum.CLOSED);
            }
        }
    }
}