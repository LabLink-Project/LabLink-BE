package com.example.lablink.category.repository;

import com.example.lablink.category.entity.Category;
import com.example.lablink.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByStudy(Study study);
}
