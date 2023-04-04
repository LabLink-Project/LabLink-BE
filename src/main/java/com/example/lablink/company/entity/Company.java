package com.example.lablink.company.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
    private String businessCategory;

    @Column(nullable = false)
    private String managerPhone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

}
