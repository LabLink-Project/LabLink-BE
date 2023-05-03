package com.example.lablink.support;

import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.user.entity.UserRoleEnum;

public enum CompanyStub {
    Company1(1L, "test@test.com", "password", "Test Company", "John Doe", "IT", "123-456-7890", "123 Test Street", UserRoleEnum.BUSINESS);

    private final Long id;
    private final String email;
    private final String password;
    private final String companyName;
    private final String ownerName;
    private final String business;
    private final String managerPhone;
    private final String address;
    private final UserRoleEnum role;

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
