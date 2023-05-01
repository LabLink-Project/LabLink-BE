package com.example.lablink.bookmark.service;

import com.example.lablink.bookmark.dto.BookmarkResponseDto;
import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.bookmark.repository.BookmarkRepository;
import com.example.lablink.company.entity.Company;
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
//        User user = isLogin(userDetails);
        User user = userDetails.getUser();
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

    public String bookmark(Long studyId, CompanyDetailsImpl companyDetails) {
        Company company = companyDetails.getCompany();
        getStudyService.getStudy(studyId);

        String result = "북마크 성공";
        if (checkBookmark(studyId, company)) {
            deleteBookmark(studyId, company);
            result = "북마크 취소";
        } else {
            saveBookmark(studyId, company);
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

    public void deleteBookmark(Long studyId, User user) {
        bookmarkRepository.deleteByStudyIdAndUser(studyId, user);
    }

    public List<Bookmark> findAllByMyBookmark(User user) {
        return bookmarkRepository.findAllByUser(user);
    }

    public void deleteAllBookmark(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    // company
    @Transactional
    public boolean checkBookmark(Long studyId, Company company) {
        return bookmarkRepository.existsByStudyIdAndCompany(studyId, company);
    }
    private void saveBookmark(Long studyId, Company company) {
        bookmarkRepository.saveAndFlush(new Bookmark(studyId, company));
    }

    public void deleteBookmark(Long studyId, Company company) {
        bookmarkRepository.deleteByStudyIdAndCompany(studyId, company);
    }

    public List<Bookmark> findAllByMyBookmark(Company company) {
        return bookmarkRepository.findAllByCompany(company);
    }

    public void deleteByStudyId(long studyId) {
        bookmarkRepository.deleteByStudyId(studyId);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getUserBookmark(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<Bookmark> bookmarks = findAllByMyBookmark(user);
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            Study study = getStudyService.getStudy(bookmark.getStudyId());
            bookmarkResponseDtos.add(new BookmarkResponseDto(study, bookmark));
        }
        return bookmarkResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponseDto> getCompanyBookmark(CompanyDetailsImpl companyDetails) {
        Company company = companyDetails.getCompany();
        List<Bookmark> bookmarks = findAllByMyBookmark(company);
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            Study study = getStudyService.getStudy(bookmark.getStudyId());
            bookmarkResponseDtos.add(new BookmarkResponseDto(study, bookmark));
        }
        return bookmarkResponseDtos;
    }
}
