package com.example.lablink.global.S3Image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.global.S3Image.entity.S3Image;
import com.example.lablink.global.S3Image.repository.S3ImageRepository;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;
    private final S3ImageRepository s3ImageRepository;

    public S3ResponseDto uploadFiles(String directory, MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        // 파일의 크기와 MIME 타입 등을 지정하는 메타데이터 담음
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        // InputStream : 바이터 단위로 데이터를 읽어오기 위한 추상클래스 - 파일 데이터를 읽을 수 있음
        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = directory + uploadFileName; // ex) 구분/년/월/일/파일.확장자
            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicReadWrite));
            // CannedAccessControlList.PublicReadWrite : 외부에 공개하는 파일인 경우 Public Read 권한을 추가, ACL 확인
            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Filed upload failed", e);
        }

        S3Image s3Image = S3Image.builder()
                .originalFileName(originalFileName)
                .uploadFileName(uploadFileName)
                .uploadFilePath(directory)
                .uploadFileUrl(uploadFileUrl)
                .build();

        s3ImageRepository.save(s3Image);

        return new S3ResponseDto(s3Image);
    }

    /**
     * S3에 업로드된 파일 삭제
     */
    public void deleteFile(Long id) {
        S3Image s3Image = s3ImageRepository.findById(id).orElseThrow(
                () -> new GlobalException(GlobalErrorCode.NOT_FOUND_IMAGE));

        try {
            String keyName = s3Image.getUploadFilePath() + s3Image.getUploadFileName(); // ex) 구분/년/월/일/파일.확장자
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, keyName);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucketName, keyName);
            } else {
                throw new GlobalException(GlobalErrorCode.NOT_FOUND_IMAGE);
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }

        s3ImageRepository.deleteById(id);
    }


    // 파일 이름의 중복을 막기위해 uuid 사용
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

}