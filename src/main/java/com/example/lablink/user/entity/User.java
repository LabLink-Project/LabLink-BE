package com.example.lablink.user.entity;

import com.example.lablink.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

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
    private String userPhone;

    @Column(unique = true)
    private String kakaoEmail;

    @Column(unique = true)
    private String naverEmail;

    @Column(unique = true)
    private String googleEmail;

//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;

    public User(String password, SignupRequestDto signupRequestDto/*, UserRoleEnum role*/) {
        this.email = signupRequestDto.getEmail();
        this.password = password;
        this.userName = signupRequestDto.getUserName();
        this.userPhone = signupRequestDto.getUserPhone();
//        this.kakaoEmail = getKakaoEmail();
//        this.naverEmail = getNaverEmail();
//        this.googleEmail = getGoogleEmail();
//        this.role = role;
    }




}
