package com.example.lablink.study.dto.responseDto;

import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class StudyDetailResponseDto {
    private final Long id;
    private final String title;
    private final String studyInfo;
    private final String studyPurpose;
    private final String studyAction;
    private final Long subjectCount;
    private final CategoryEnum category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime date;
    private final String address;
    private final int pay;
    private final String subjectGender;
    private final int subjectMinAge;
    private final int subjectMaxAge;
    private final int repearCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime endDate;

    private final String imageURL;
    private final boolean isbookmarked;

    public StudyDetailResponseDto(Study study, boolean isbookmarked) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.studyInfo = study.getStudyInfo();
        this.studyPurpose = study.getStudyPurpose();
        this.studyAction = study.getStudyAction();
        this.subjectCount = study.getSubjectCount();
        this.category = study.getCategory();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.pay = study.getPay();
        this.subjectGender = study.getSubjectGender();
        this.subjectMinAge = study.getSubjectMinAge();
        this.subjectMaxAge = study.getSubjectMaxAge();
        this.repearCount = study.getRepearCount();
        this.endDate = study.getEndDate();
        this.imageURL = study.getImageURL();
        this.isbookmarked = isbookmarked;
    }
}
