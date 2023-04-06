package com.example.lablink.bookmark.service;

import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.bookmark.repository.BookmarkRepository;
import com.example.lablink.study.service.StudyService;
import com.example.lablink.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final StudyService studyService;
    public String bookmark(Long studyId, User user) {
        studyService.getStudy(studyId);

        String result = "북마크 성공";
        if (checkBookmark(studyId, user)){
            deleteBookmark(studyId, user);
            result = "북마크 취소";
        } else {
            saveBookmark(studyId, user);
        }
        return result;
    }

    @Transactional
    public boolean checkBookmark(Long studyId, User user) {
        return bookmarkRepository.existsByStudyIdAndUser(studyId, user);
    }

    private void saveBookmark(Long studyId, User user) {
        bookmarkRepository.saveAndFlush(new Bookmark(studyId, user));
    }
    private void deleteBookmark(Long studyId, User user) {
        bookmarkRepository.deleteByStudyIdAndUser(studyId, user);
    }
}
