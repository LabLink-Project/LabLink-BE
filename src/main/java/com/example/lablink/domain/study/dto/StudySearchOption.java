package com.example.lablink.domain.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
public class StudySearchOption {
    private String category;
    private String address;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime searchTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchDate;
    private String gender;
    private String age;
    private String keyword;

    // 사용자 정의 메서드
    public boolean hasValue() {
        return category != null || address != null || searchDate != null || searchTime != null || gender != null || age != null || keyword != null;
    }
}

