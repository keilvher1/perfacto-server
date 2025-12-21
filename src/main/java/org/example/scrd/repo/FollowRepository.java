package org.example.scrd.repo;

import org.example.scrd.domain.Follow;
import org.example.scrd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 팔로우 관계 존재 확인
     */
    boolean existsByFollowerAndFollowing(User follower, User following);

    /**
     * 특정 팔로우 관계 조회
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    /**
     * 팔로워가 팔로우하는 모든 사람 조회
     */
    List<Follow> findAllByFollower(User follower);

    /**
     * 특정 사용자를 팔로우하는 모든 사람 조회
     */
    List<Follow> findAllByFollowing(User following);

    /**
     * 팔로잉 수 카운트
     */
    long countByFollower(User follower);

    /**
     * 팔로워 수 카운트
     */
    long countByFollowing(User following);

    /**
     * 팔로우 관계 삭제
     */
    void deleteByFollowerAndFollowing(User follower, User following);

    /**
     * 팔로잉 ID 목록 조회
     */
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByUserId(@Param("userId") Long userId);
}
