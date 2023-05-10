package com.example.lablink.domain.user.entity;

import com.example.lablink.domain.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.global.timestamp.entity.Timestamped;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "users")
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE users SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    // xxx :  카카오 로그인은 password 없으니까 .. ?
    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private String userGender;

    // xxx : kakaoid 추가했슴니다 ..
    @Column(nullable = true, unique = true)
    private Long kakaoId;

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

    // 회원가입시
    public User(SignupRequestDto signupRequestDto, String password, UserInfo userinfo, UserRoleEnum role) {
        this.email = signupRequestDto.getEmail();
        this.nickName = signupRequestDto.getNickName();
        this.password = password;
        this.userName = getUserName();
        this.dateOfBirth = getDateOfBirth();
        this.userGender = getUserGender();
        this.userinfo = userinfo;
        this.role = role;
    }

    // 카카오 로그인시
    public User(Long kakaoId, String nickname, String email, UserInfo userInfo, UserRoleEnum role){
        this.kakaoId = kakaoId;
        this.nickName = nickname;
        if(email != null) this.kakaoEmail = email;
        this.userinfo = userInfo;
        this.role = role;
    }

    // 신청서 작성시
    public void updateUser(ApplicationRequestDto applicationRequestDto) {
        this.userName = applicationRequestDto.getUserName();
        this.dateOfBirth = applicationRequestDto.getDateOfBirth();
        this.userGender = applicationRequestDto.getUserGender();
    }

    public User googleIdUpdate(String googleEmail) {
        this.googleEmail = googleEmail;
        return this;
    }

    public User(String username, String password, String nickname, UserRoleEnum role, String googleEmail) {
        this.userName = username;
        this.password = password;
        this.nickName = nickname;
        this.role = role;
        this.googleEmail = googleEmail;
    }

}
