package com.example.lablink.study.dto.responseDto;

import com.example.lablink.category.entity.Category;
import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import lombok.Getter;

import java.time.LocalDate;

// TODO : 기업인증, 기업온도
// TODO: region or address?
@Getter
public class StudyResponseDto {
    private final Long id;
    private final String title;
    private final Category category;
    private final LocalDate date;
    private final String address;
    private final String pay;
    private final String companyName;

    public StudyResponseDto(Study study, Category category) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.category = category;
        this.date = study.getDate();
        this.pay = study.getPay();
        this.address = study.getAddress();
        this.companyName = study.getCompany().getCompanyName();
    }
}
