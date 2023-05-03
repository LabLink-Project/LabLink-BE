package com.example.lablink.global.S3Image.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class S3Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;
}