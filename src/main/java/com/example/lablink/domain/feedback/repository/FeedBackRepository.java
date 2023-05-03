package com.example.lablink.domain.feedback.repository;

import com.example.lablink.domain.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllByStudyId(Long id);
}
