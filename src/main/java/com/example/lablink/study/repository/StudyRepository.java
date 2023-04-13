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

    @Query(value = "select * from study " +
        "where (:category is null or lower(category) like lower(concat('%', :category, '%'))) " +
        "and (:address is null or lower(address) like lower(concat('%', :address, '%'))) " +
        "and (:searchDate is null or lower(date) like lower(concat('%', :searchDate, '%'))) " +
        "and (:searchTime is null or lower(date) like lower(concat('%', :searchTime, '%'))) " +
        "and (:gender is null or lower(subject_gender) like lower(concat('%', :gender, '%'))) " +
        "and ((:age is null) or ((:age > subject_min_age) and (:age < subject_max_age))) " +
        "and (:keyword is null or lower(title) like lower(concat('%', :keyword, '%'))) " +
        "order by created_at desc " +
        "limit :pageIndex, :pageCount",
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

        return searchStudiesNativeQuery(category, address, searchDate, searchTime, gender, age, keyword, pageIndex, pageCount);
    }

}

