package org.example.scrd.repo;

import org.example.scrd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByAppleId(String appleId);
    Optional<User> findByNaverId(String naverId);
    Optional<User> findByEmail(String email);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);

    /**
     * 닉네임으로 사용자 검색 (부분 일치, 대소문자 무시)
     */
    List<User> findByNickNameContainingIgnoreCase(String nickName);

    /**
     * 이름으로 사용자 검색 (부분 일치, 대소문자 무시)
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * 닉네임 또는 이름으로 사용자 검색 (부분 일치, 대소문자 무시)
     */
    List<User> findByNickNameContainingIgnoreCaseOrNameContainingIgnoreCase(String nickName, String name);
}
