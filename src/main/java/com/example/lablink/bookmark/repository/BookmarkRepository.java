package com.example.lablink.bookmark.repository;

import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByStudyIdAndUser(Long studyId, User user);
    boolean existsByStudyIdAndUser(Long studyId, User user);

    List<Bookmark> findAllByUser(User user);

}
