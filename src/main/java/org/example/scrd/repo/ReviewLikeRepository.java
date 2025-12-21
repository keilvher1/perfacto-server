package org.example.scrd.repo;

import org.example.scrd.domain.PerfactoReview;
import org.example.scrd.domain.ReviewLike;
import org.example.scrd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    /**
     * 리뷰 좋아요 존재 확인
     */
    boolean existsByUserAndReview(User user, PerfactoReview review);

    /**
     * 특정 사용자의 리뷰 좋아요 조회
     */
    Optional<ReviewLike> findByUserAndReview(User user, PerfactoReview review);

    /**
     * 리뷰의 좋아요 수 카운트
     */
    long countByReview(PerfactoReview review);

    /**
     * 리뷰 좋아요 삭제
     */
    void deleteByUserAndReview(User user, PerfactoReview review);
}
