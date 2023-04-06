package com.example.lablink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LabLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabLinkApplication.class, args);
    }

}
