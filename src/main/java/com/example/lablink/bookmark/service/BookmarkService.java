package com.example.lablink.bookmark.service;

import com.example.lablink.bookmark.dto.BookmarkResponseDto;
import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.bookmark.repository.BookmarkRepository;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.study.service.GetStudyService;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final GetStudyService getStudyService;

    @Transactional
    public String bookmark(Long studyId, UserDetailsImpl userDetails) {
        User user = isLogin(userDetails);
        getStudyService.getStudy(studyId);

        String result = "북마크 성공";
        if (checkBookmark(studyId, user)) {
            deleteBookmark(studyId, user);
            result = "북마크 취소";
        } else {
            saveBookmark(studyId, user);
        }
        return result;
    }

    private User isLogin(UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return userDetails.getUser();
        } else {
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
    }

    @Transactional
    public boolean checkBookmark(Long studyId, User user) {
        return bookmarkRepository.existsByStudyIdAndUser(studyId, user);
    }

    private void saveBookmark(Long studyId, User user) {
        bookmarkRepository.saveAndFlush(new Bookmark(studyId, user));
    }

    public void deleteBookmark(Long studyId, User user) {
        bookmarkRepository.deleteByStudyIdAndUser(studyId, user);
    }

    public List<Bookmark> findAllByMyBookmark(User user) {
        return bookmarkRepository.findAllByUser(user);
    }

    public void deleteAllBookmark(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    ;

    public void deleteByStudyId(long studyId) {
        bookmarkRepository.deleteByStudyId(studyId);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getUserBookmark(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
        User user = userDetails.getUser();
        List<Bookmark> bookmarks = findAllByMyBookmark(user);
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            Study study = getStudyService.getStudy(bookmark.getStudyId());
            bookmarkResponseDtos.add(new BookmarkResponseDto(study, bookmark));
        }
        return bookmarkResponseDtos;
    }
}
