package com.example.lablink.feedback.repository;

import com.example.lablink.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<Feedback,Long> {
}
