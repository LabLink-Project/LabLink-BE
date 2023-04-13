package com.example.lablink.feedback.repository;

import com.example.lablink.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllByStudyId(Long id);

    /*@Query("select l from Feedback l where l.user.userName = :user_Id and l.study.id = :studyId")
    List<Feedback> findAllByStudyId(Long id);*/
}
