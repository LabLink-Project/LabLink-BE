package com.example.lablink.domain.study.repository;

import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.study.dto.StudySearchOption;
import com.example.lablink.domain.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Study> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Study> findAllByOrderByPayDesc(Pageable pageable);
    Page<Study> findAllByOrderByCurrentApplicantCountDesc(Pageable pageable);
    List<Study> findAllByCompany(Company company);

    @Query(value = "select * from study as s where s.end_date < now()", nativeQuery = true)
    List<Study> getAllByEndDate();

    // done : 방금 올린 공고 못 찾는 이슈 해결 ..
//    @Query(value = "ALTER TABLE study ADD FULLTEXT key (title, study_info, study_purpose, study_action)", nativeQuery = true);


    // todo : offset -> limit, lastid로 바꿔주기
    //  offset은 fullscan이기 때문에 성능 저하를 일으킨다
    //  무한스크롤시 몇번째 페이지로 이동이 없기 때문에 굳이 fullscan을 할 필요 없음
    @Query(value = "SELECT * FROM study " +
            "WHERE (:category is null or lower(category) like lower(concat('%', :category, '%'))) " +
            "AND (:address is null or lower(address) like lower(concat('%', :address, '%'))) " +
            "AND (:searchDate is null or lower(date) like lower(concat('%', :searchDate, '%'))) " +
            "AND (:searchTime is null or lower(date) like lower(concat('%', :searchTime, '%'))) " +
            "AND (:gender is null or lower(subject_gender) like lower(concat('%', :gender, '%'))) " +
            "AND ((:age is null) or ((:age >= subject_min_age) and (:age <= subject_max_age))) " +
            "AND (:keyword is null or lower(title) like lower(concat('%', :keyword, '%'))) " +
            "ORDER BY created_at DESC " +
            "LIMIT :pageCount OFFSET :offset",
            nativeQuery = true)

    List<Study> searchStudiesNativeQuery(
            @Param("category") String category,
            @Param("address") String address,
            @Param("searchDate") LocalDate searchDate,
            @Param("searchTime") LocalTime searchTime,
            @Param("gender") String gender,
            @Param("age") String age,
            @Param("keyword") String keyword,
            @Param("offset") int offset,
//            @Param("pageIndex") int pageIndex,
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
        int offset = (pageIndex - 1) * pageCount;

        return searchStudiesNativeQuery(category, address, searchDate, searchTime, gender, age, keyword, offset, pageCount);
    }
}