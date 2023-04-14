package com.example.lablink.user.entity;

import com.example.lablink.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.timestamp.entity.Timestamped;
import com.example.lablink.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private Date dateOfBirth;

    @Column(nullable = true)
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

    // 회원가입시
    public User(SignupRequestDto signupRequestDto, String password, UserInfo userinfo, UserRoleEnum role) {
        this.email = signupRequestDto.getEmail();
        this.password = password;
        this.userName = getUserName();
        this.dateOfBirth = getDateOfBirth();
        this.userGender = getUserGender();
        this.userinfo = userinfo;
        this.role = role;
    }

    // 신청서 작성시
    public void updateUser(ApplicationRequestDto applicationRequestDto) {
        this.userName = applicationRequestDto.getUserName();
        this.dateOfBirth = applicationRequestDto.getDateOfBirth();
        this.userGender = applicationRequestDto.getUserGender();
    }

}