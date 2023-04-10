package com.example.lablink.user.entity;

import com.example.lablink.timestamp.entity.Timestamped;
import com.example.lablink.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "users")
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
@Getter
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
    private String userGender;

    @Column(unique = true)
    private String kakaoEmail;

    @Column(unique = true)
    private String naverEmail;

    @Column(unique = true)
    private String googleEmail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToOne
    @JoinColumn(name = "userinfo_id", nullable = false)
    private UserInfo userinfo;

    public User(String password, SignupRequestDto signupRequestDto, UserRoleEnum role) {
        this.email = signupRequestDto.getEmail();
        this.password = password;
        this.userName = signupRequestDto.getUserName();
        this.dateOfBirth = signupRequestDto.getDateOfBirth();
        this.userGender = signupRequestDto.getUserGender();
//        this.kakaoEmail = getKakaoEmail();
//        this.naverEmail = getNaverEmail();
//        this.googleEmail = getGoogleEmail();
        this.role = role;
    }

//    public User(String password, SignupRequestDto signupRequestDto, UserRoleEnum role, UserInfo userinfo) {
//        this.email = signupRequestDto.getEmail();
//        this.password = password;
//        this.userName = signupRequestDto.getUserName();
//        this.dateOfBirth = signupRequestDto.getDateOfBirth();
//        this.userGender = signupRequestDto.getUserGender();
////        this.kakaoEmail = getKakaoEmail();
////        this.naverEmail = getNaverEmail();
////        this.googleEmail = getGoogleEmail();
//        this.role = role;
//        this.userinfo = userinfo;
//    }

}