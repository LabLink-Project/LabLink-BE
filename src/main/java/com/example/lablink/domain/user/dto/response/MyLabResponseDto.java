package com.example.lablink.domain.user.dto.response;

import com.example.lablink.domain.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyLabResponseDto {
    private Long id; // applicationId
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime applicationDate; // 신청 날짜 -> timestamped create
    private int pay;
    private String address;
    private String viewStatus; // 기업이 내 신청서에 대한 "열람", "미열람"
    private String approvalStatus; // 기업이 내 신청에대한 "승인", "거절", "대기"
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime date; // 진행한, 진행할 테스트 날짜
    private String companyName;

    public MyLabResponseDto(Study study, Long id, String viewStatus, String approvalStatus) {
        this.id = id;
        this.companyName = study.getCompany().getCompanyName();
        this.title = study.getTitle();
        this.applicationDate = study.getCreatedAt();
        this.pay = study.getPay();
        this.address = study.getAddress();
        this.viewStatus = viewStatus;
        this.approvalStatus = approvalStatus;
        this.date = study.getDate();
    }
}