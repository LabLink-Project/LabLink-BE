package com.example.lablink.application.repository;

import com.example.lablink.application.entity.Application;
import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Optional<Application> findByIdAndUserEmail(Long Id,String userEmail);
    List<Application> findAllByUser(User user);

}
