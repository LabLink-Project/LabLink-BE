package com.example.lablink.domain.user.repository;

import com.example.lablink.domain.user.entity.User;
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

    // 마이 페이지 신청서조회 jpql
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


}
