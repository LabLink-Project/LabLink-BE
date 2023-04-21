package com.example.lablink.support;

import com.example.lablink.company.entity.Company;
import com.example.lablink.user.entity.UserRoleEnum;

import javax.persistence.*;

public enum CompanyStub {
    Company1(1L, "test@test.com", "password", "Test Company", "John Doe", "IT", "123-456-7890", "123 Test Street", UserRoleEnum.BUSINESS);

    private Long id;
    private String email;
    private String password;
    private String companyName;
    private String ownerName;
    private String business;
    private String managerPhone;
    private String address;
    private UserRoleEnum role;

    CompanyStub(Long id, String email, String password, String companyName, String ownerName, String business, String managerPhone, String address, UserRoleEnum role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.ownerName = ownerName;
        this.business = business;
        this.managerPhone = managerPhone;
        this.address = address;
        this.role = role;
    }

    public Company of(){
        return Company.builder()
                .id(null)
                .email(this.email)
                .password(this.password)
                .companyName(this.companyName)
                .ownerName(this.ownerName)
                .business(this.business)
                .managerPhone(this.managerPhone)
                .address(this.address)
                .role(this.role)
                .build();
    }
}
