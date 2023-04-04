package com.example.lablink.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
public class StudySearchOption {
    private String category;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchDate;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime searchTime;
    private String gender;
    private String age;
    private String keyword;
}

