package com.example.lablink.company.dto.response;

import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ViewMyStudyResponseDto {
    private String title;
    private CategoryEnum category;
    private LocalDateTime date;
    private int pay;
    private String address;
    private StudyStatusEnum studyStatus;
    private Long subjectCount;
    private LocalDateTime endDate;

    public ViewMyStudyResponseDto(Study study) {
        this.title = study.getTitle();
        this.category = study.getCategory();
        this.date = study.getDate();
        this.pay = study.getPay();
        this.address = study.getAddress();
        this.studyStatus = study.getStatus();
        this.subjectCount = study.getSubjectCount();
        this.endDate = study.getEndDate();
    }
}
