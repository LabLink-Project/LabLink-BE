package com.example.lablink.domain.company.dto.response;

import com.example.lablink.domain.study.entity.CategoryEnum;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.entity.StudyStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ViewMyStudyResponseDto {
    private Long id;
    private String title;
    private CategoryEnum category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private int pay;
    private String address;
    private StudyStatusEnum studyStatus;
    private Long subjectCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    public ViewMyStudyResponseDto(Study study) {
        this.id = study.getId();
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
