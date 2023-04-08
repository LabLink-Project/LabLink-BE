package com.example.lablink.study.dto.responseDto;

import com.example.lablink.category.entity.Category;
import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// TODO : 기업온도 (추가 기능으로 변경)
// region or address? -> address!
@Getter
public class StudyResponseDto {
    private final Long id;
    private final String title;
    private final String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime date;

    private final String address;
    private final int pay;
    private final StudyStatusEnum studyStatusEnum;
    private final String companyName;
    private final boolean isbookmarked;

    public StudyResponseDto(Study study, Category category, boolean isbookmarked) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.category = category.getCategory();
        this.date = study.getDate();
        this.pay = study.getPay();
        this.address = study.getAddress();
        this.companyName = study.getCompany().getCompanyName();
        this.studyStatusEnum = study.getStatus();
        this.isbookmarked = isbookmarked;
    }
}
