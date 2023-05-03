package com.example.lablink.domain.user.repository;

import com.example.lablink.domain.user.entity.Terms;
import com.example.lablink.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    void deleteByUser(User user);
}
