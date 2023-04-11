package com.example.lablink.user.entity;

import com.example.lablink.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String userAdrress;

    @Column(nullable = false)
    private String userPhone;

    public UserInfo(SignupRequestDto signupRequestDto) {
        this.userPhone = signupRequestDto.getUserPhone();
    }
}
