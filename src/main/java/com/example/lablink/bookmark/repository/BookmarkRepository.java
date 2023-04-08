package com.example.lablink.bookmark.repository;

import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByStudyIdAndUser(Long studyId, User user);
    boolean existsByStudyIdAndUser(Long studyId, User user);
}
