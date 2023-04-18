package com.example.lablink.user.dto.response;

import com.example.lablink.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyLabResponseDto {
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime applicationDate; // 신청 날짜 -> timestamped create
    private int pay;
    private String address;
    private String viewStatus; // 기업이 내 신청서에 대한 "열람", "미열람"
    private String approvalStatus; // 기업이 내 신청에대한 "승인", "거절", "대기"
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date; // 진행한, 진행할 테스트 날짜

    public MyLabResponseDto(Study study, String approvalStatus, String viewStatus) {
        this.title = study.getTitle();
        this.applicationDate = study.getCreatedAt();
        this.pay = study.getPay();
        this.address = study.getAddress();
        this.viewStatus = viewStatus;
        this.approvalStatus = approvalStatus;
        this.date = study.getDate();
    }
}