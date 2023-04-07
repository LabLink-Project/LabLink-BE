package com.example.lablink.category.repository;

import com.example.lablink.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByStudyId(Long studyId);
}
