package com.example.lablink.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyLabResponseDto {
    private Long id;
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

    public MyLabResponseDto(Long id, String title, LocalDateTime applicationDate, int pay, String address, String viewStatus, String approvalStatus, LocalDateTime date, String companyName) {
        this.id = id;
        this.title = title;
        this.applicationDate = applicationDate;
        this.pay = pay;
        this.address = address;
        this.viewStatus = viewStatus;
        this.approvalStatus = approvalStatus;
        this.date = date;
        this.companyName = companyName;
    }
}