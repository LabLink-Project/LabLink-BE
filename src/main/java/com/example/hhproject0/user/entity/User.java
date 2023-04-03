package com.example.hhproject0.user.entity;

import com.example.hhproject0.user.dto.request.SignupRequestDto;
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
    private String username;

    @Column(nullable = false)
    private String userPhone;

    @Column(nullable = false, unique = true)
    private String kakaoEmail;

    @Column(nullable = false, unique = true)
    private String naverEmail;

    @Column(nullable = false, unique = true)
    private String googleEmail;

    @OneToOne
    @JoinColumn(name = "terms_id", nullable = false)
    private Terms terms;

    public User(String email, String password, SignupRequestDto signupRequestDto, Terms terms) {
        this.email = email;
        this.password = password;
        this.username = signupRequestDto.getUserName();
        this.userPhone = signupRequestDto.getUserPhone();
        this.kakaoEmail = getKakaoEmail();
        this.naverEmail = getNaverEmail();
        this.googleEmail = getGoogleEmail();
        this.terms = terms;

    }


//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;



}
