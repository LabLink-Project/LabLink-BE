package com.example.lablink.study.dto.requestDto;

import com.example.lablink.study.entity.StudyStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudyRequestDto {
    private String title;
    private String studyInfo;
    private String studyPurpose;
    private String studyAction;
    private Long subjectCount;
    private String category;
    private LocalDate date;
    private String address;
    private String pay;
    private String gender;
    private String age;
    private int repearCount;
    private LocalDate endDate;
    private String imageURL;
}
