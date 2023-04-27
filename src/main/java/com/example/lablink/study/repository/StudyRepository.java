package com.example.lablink.study.repository;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findAllByOrderByEndDateDesc();
    Optional<Study> findByIdAndCompany(Long id, Company company);
    List<Study> findAllByOrderByCreatedAtDesc();
    List<Study> findAllByOrderByPayDesc();
    List<Study> findAllByOrderByCurrentApplicantCountDesc();
    List<Study> findAllByCompany(Company company);

    List<Study> findAllByEmailSendIsFalse();

    // todo : 방금 올린 공고 못 찾는 이슈 해결 ..
//    @Query(value = "ALTER TABLE study ADD FULLTEXT key (title, study_info, study_purpose, study_action)", nativeQuery = true);

    @Query(value = "SELECT * FROM study " +
            "WHERE (:category IS NULL OR LOWER(category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:address IS NULL OR LOWER(address) LIKE LOWER(CONCAT('%', :address, '%'))) " +
            "AND (:searchDate IS NULL OR LOWER(date) LIKE LOWER(CONCAT('%', :searchDate, '%'))) " +
            "AND (:searchTime IS NULL OR LOWER(date) LIKE LOWER(CONCAT('%', :searchTime, '%'))) " +
            "AND (:gender IS NULL OR LOWER(subject_gender) LIKE LOWER(CONCAT('%', :gender, '%'))) " +
            "AND ((:age IS NULL) OR ((:age >= subject_min_age) AND (:age <= subject_max_age))) " +
            // todo : fulltext.. 123qweasd -> 123qwe으로 검색하면 안나옴 수정
            "AND (:keyword IS NULL OR LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:keyword IS NULL OR MATCH (title, study_info, study_purpose, study_action) AGAINST (:keyword IN BOOLEAN MODE)) " +
//            "AND (:keyword IS NULL OR MATCH(title, study_info, study_purpose, study_action) AGAINST(:keyword IN BOOLEAN MODE)) " +
            "ORDER BY created_at DESC " +
            "LIMIT :pageIndex, :pageCount",
            nativeQuery = true)

    List<Study> searchStudiesNativeQuery(
            @Param("category") String category,
            @Param("address") String address,
            @Param("searchDate") LocalDate searchDate,
            @Param("searchTime") LocalTime searchTime,
            @Param("gender") String gender,
            @Param("age") String age,
            @Param("keyword") String keyword,
            @Param("pageIndex") int pageIndex,
            @Param("pageCount") int pageCount
    );

    // StudySearchOption에 맞는 검색 기능 추가
    default List<Study> searchStudiesBySearchOption(StudySearchOption searchOption, int pageIndex, int pageCount) {
        String category = searchOption.getCategory();
        String address = searchOption.getAddress();
        LocalDate searchDate = searchOption.getSearchDate();
        LocalTime searchTime = searchOption.getSearchTime();
        String gender = searchOption.getGender();
        String age = searchOption.getAge();
        String keyword = searchOption.getKeyword();
//        if (keyword != null) {
//            keyword = keyword.replace("+", " +").replace("-", " -").replace("\"", "");
//        }
        return searchStudiesNativeQuery(category, address, searchDate, searchTime, gender, age, keyword, pageIndex, pageCount);
    }

}