package com.example.lablink.study.dto.responseDto;

import com.example.lablink.application.entity.Application;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import lombok.Getter;

import java.util.Date;

@Getter
public class ApplicationFromStudyResponseDto {

    private Long id;
    private String userName;
    private String userPhone;
    private String userGender;
    private Date dateOfBirth;
    private String userAddress;
    private String message;

    public ApplicationFromStudyResponseDto(User user, UserInfo userInfo , Application applicationString) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userPhone = userInfo.getUserPhone();
        this.userGender = user.getUserGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.userAddress = userInfo.getUserAddress();
        this.message = applicationString.getMessage();
    }
}
