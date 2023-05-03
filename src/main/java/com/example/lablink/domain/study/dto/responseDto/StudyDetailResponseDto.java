package com.example.lablink.domain.study.dto.responseDto;

import com.example.lablink.domain.study.entity.CategoryEnum;
import com.example.lablink.domain.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudyDetailResponseDto {
    private final Long id;
    private final String title;
    private final String studyInfo;
    private final String description;
    private final String benefit;
    private final CategoryEnum category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime date;
    private final String address;
    private final int pay;
    private final String subjectGender;
    private final int subjectMinAge;
    private final int subjectMaxAge;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime endDate;

    private final String thumbnailImageURL;
    private final String detailImageURL;

    private final boolean isbookmarked;
    private final int currentApplicantCount;
    private final String companyName;
    private final boolean isapplied;

    public StudyDetailResponseDto(Study study, boolean isbookmarked, boolean isapplied) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.studyInfo = study.getStudyInfo();
        this.description = study.getDescription();
        this.benefit = study.getBenefit();
        this.category = study.getCategory();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.pay = study.getPay();
        this.subjectGender = study.getSubjectGender();
        this.subjectMinAge = study.getSubjectMinAge();
        this.subjectMaxAge = study.getSubjectMaxAge();
        this.endDate = study.getEndDate();
        this.thumbnailImageURL = study.getThumbnailImageURL();
        this.detailImageURL = study.getDetailImageURL();
        this.isbookmarked = isbookmarked;
        this.currentApplicantCount = study.getCurrentApplicantCount();
        this.companyName = study.getCompany().getCompanyName();
        this.isapplied = isapplied;
    }
}
