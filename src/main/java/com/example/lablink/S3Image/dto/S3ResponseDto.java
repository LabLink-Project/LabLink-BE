package com.example.lablink.S3Image.dto;

import com.example.lablink.S3Image.entity.S3Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3ResponseDto {
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

    public S3ResponseDto(S3Image s3Image) {
        this.originalFileName = s3Image.getOriginalFileName();
        this.uploadFileName = s3Image.getUploadFileName();
        this.uploadFilePath = s3Image.getUploadFilePath();
        this.uploadFileUrl = s3Image.getUploadFileUrl();
    }
}