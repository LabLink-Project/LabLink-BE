package com.example.lablink.domain.company.entity;

import com.example.lablink.domain.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.domain.user.entity.UserRoleEnum;
import com.example.lablink.global.timestamp.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE company SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
public class Company extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String business;

    @Column(nullable = false)
    private String managerPhone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String logoUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public Company(String password, CompanySignupRequestDto companySignupRequestDto, String logoUrl, UserRoleEnum role) {
        this.email = companySignupRequestDto.getEmail();
        this.password = password;
        this.companyName = companySignupRequestDto.getCompanyName();
        this.ownerName = companySignupRequestDto.getOwnerName();
        this.business = companySignupRequestDto.getBusiness();
        this.managerPhone = companySignupRequestDto.getManagerPhone();
        this.address = companySignupRequestDto.getAddress();
        this.detailAddress = companySignupRequestDto.getDetailAddress();
        this.logoUrl = Objects.requireNonNullElse(logoUrl, "https://cdn.icon-icons.com/icons2/931/PNG/512/empty_file_icon-icons.com_72420.png");
        this.role = role;
    }

    @Builder
    public Company(Long id, String email, String password, String companyName, String ownerName, String business, String managerPhone, String address, String detailAddress, UserRoleEnum role){
        this.id = id;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.ownerName = ownerName;
        this.business = business;
        this.managerPhone = managerPhone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.role = role;
    }

}
