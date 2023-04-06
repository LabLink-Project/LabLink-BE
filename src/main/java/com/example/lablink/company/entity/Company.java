package com.example.lablink.company.entity;

import com.example.lablink.company.dto.request.CompanySignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String business;

    @Column(nullable = false)
    private String managerPhone;

    @Column(nullable = false)
    private String address;

    public Company(String password, CompanySignupRequestDto companySignupRequestDto) {
        this.email = companySignupRequestDto.getEmail();
        this.password = password;
        this.companyName = companySignupRequestDto.getCompanyName();
        this.ownerName = companySignupRequestDto.getOwnerName();
        this.business = companySignupRequestDto.getBusiness();
        this.managerPhone = companySignupRequestDto.getManagerPhone();
        this.address = companySignupRequestDto.getAddress();
    }
}