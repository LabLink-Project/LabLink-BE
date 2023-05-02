package com.example.lablink.user.repository;

import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String userEmail);
    boolean existsByEmail(String userEmail);
    boolean existsByNickName(String nickName);
    Optional<User> findByKakaoId(Long kakaoId);
    boolean existsByKakaoId(Long kakaoId);

    Optional<User> findByNickName(String nickName);
    Optional<User> findByGoogleEmail(String email);

    @Modifying
    @Query(value = "update users u " +
        "left join bookmark b on u.id = b.user_id " +
        "left join terms t on u.id = t.user_id " +
        "left join application a on u.id = a.user_id " +
        "set u.deleted_at = now(), " +
        "b.deleted_at = now(), " +
        "t.deleted_at = now(), " +
        "a.deleted_at = now() " +
        "where u.id = :userId", nativeQuery = true)
    void deleteUserAndData(@Param("userId") Long userId);

    // native쿼리 사용 방식 -> from에 테이블 입력
    // 그냥 쿼리문은 from에 객체(Entity) 입력
//    @Query(value = "select * from users where email = :email" , nativeQuery = true )
//    Optional<User> findByUserEmail(@Param("email") String email);

    // jpa 사용 방식
//    Optional<User> findByUserEmail(String email);

    // 그냥 쿼리문은 객체를 대상
//    @Query(value = "SELECT s FROM Study s " +
//        "WHERE (:category is null or s.category = :category) " +
//        "AND (:address is null or s.address = :address) " +
//        "AND (:searchDate is null or s.searchDate = :searchDate) " +
//        "AND (:searchTime is null or s.searchTime = :searchTime) " +
//        "AND (:gender is null or s.gender = :gender) " +
//        "AND (:age is null or s.age = :age) " +
//        "AND (:keyword is null or lower(s.keyword) like lower(concat('%', :keyword, '%')))")
//    List<Study> search(@Param("category") String category,
//                       @Param("address") String address,
//                       @Param("searchDate") String searchDate,
//                       @Param("searchTime") String searchTime,
//                       @Param("gender") String gender,
//                       @Param("age") String age,
//                       @Param("keyword") String keyword);
}
