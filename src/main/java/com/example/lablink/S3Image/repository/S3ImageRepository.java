package com.example.lablink.S3Image.repository;

import com.example.lablink.S3Image.entity.S3Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface S3ImageRepository extends JpaRepository<S3Image,Long> {

}
