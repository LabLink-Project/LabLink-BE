package com.example.lablink.application.dto.Response;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class ApplicationResponseDto {
    private Long studyId;
    private String title;
    private String companyName;
    private LocalDateTime date;
    private String address;
    private Long userId;
    private String username;
    private String userPhone;
    private String userEmail;
    private String userGender;
    private String userAge;

    public ApplicationResponseDto(Company company, Study study, User user) {
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.companyName = company.getCompanyName();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.userId = user.getId();
        this.username = user.getUserName();
//        this.userPhone = user.getUserPhone();
        this.userEmail = user.getEmail();
    }
}
