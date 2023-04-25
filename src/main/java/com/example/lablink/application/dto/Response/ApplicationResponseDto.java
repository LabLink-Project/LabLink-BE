package com.example.lablink.application.dto.Response;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class ApplicationResponseDto {
    private Long studyId;
    private String title;
    private String companyName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private String address;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userGender;
    private String userAge;
    private String applicationStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date dateOfBirth;
    private String userAddress;


    public ApplicationResponseDto(Company company, Study study, User user,UserInfo userInfo) {
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.companyName = company.getCompanyName();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.userName = user.getUserName();
        this.userGender = user.getUserGender();
        this.userEmail = user.getEmail();
        this.dateOfBirth = user.getDateOfBirth();
        this.userPhone = userInfo.getUserPhone();
        this.userAddress =userInfo.getUserAddress();
        this.applicationStatus = getApplicationStatus();
    }
}
