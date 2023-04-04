package com.example.lablink.study.dto.responseDto;

import com.example.lablink.category.entity.Category;
import com.example.lablink.study.entity.Study;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudyDetailResponseDto {
    private final Long id;
    private final String title;
    private final String studyInfo;
    private final String studyPurpose;
    private final String studyAction;
    private final Long subjectCount;
    private final Category category;
    private final LocalDate date;
    private final String address;
    private final String pay;
    private final String gender;
    private final String age;
    private final int repearCount;
    private final LocalDate endDate;
    private final String imageURL;

    public StudyDetailResponseDto(Study study, Category category) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.studyInfo = study.getStudyInfo();
        this.studyPurpose = study.getStudyPurpose();
        this.studyAction = study.getStudyAction();
        this.subjectCount = study.getSubjectCount();
        this.category = category;
        this.date = study.getDate();
        this.address = study.getAddress();
        this.pay = study.getPay();
        this.gender = study.getGender();
        this.age = study.getAge();
        this.repearCount = study.getRepearCount();
        this.endDate = study.getEndDate();
        this.imageURL = study.getImageURL();
    }
}
