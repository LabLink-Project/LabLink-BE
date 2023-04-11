package com.example.lablink.user.repository;

import com.example.lablink.user.entity.Terms;
import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    void deleteByUser(User user);
}
