package com.example.lablink.global.S3Image.service;

import com.example.lablink.global.S3Image.entity.S3Image;
import com.example.lablink.global.S3Image.repository.S3ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3ImageRepository s3ImageRepository;

    @Transactional
    public S3Image getS3Image(String uploadFileUrl){
        return s3ImageRepository.findByUploadFileUrl(uploadFileUrl).orElse(null);
    }
}
