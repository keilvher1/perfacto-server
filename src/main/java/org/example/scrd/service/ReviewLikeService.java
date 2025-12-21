package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.PerfactoReview;
import org.example.scrd.domain.ReviewLike;
import org.example.scrd.domain.User;
import org.example.scrd.repo.PerfactoReviewRepository;
import org.example.scrd.repo.ReviewLikeRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리뷰 좋아요 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final PerfactoReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 좋아요
     */
    @Transactional
    public void likeReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 이미 좋아요 했는지 확인
        if (reviewLikeRepository.existsByUserAndReview(user, review)) {
            throw new IllegalStateException("이미 좋아요한 리뷰입니다.");
        }

        // 좋아요 생성
        ReviewLike reviewLike = ReviewLike.create(user, review);
        reviewLikeRepository.save(reviewLike);

        // 리뷰 좋아요 수 증가
        review.incrementLikeCount();
        reviewRepository.save(review);
    }

    /**
     * 리뷰 좋아요 취소
     */
    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 좋아요 찾기
        ReviewLike reviewLike = reviewLikeRepository.findByUserAndReview(user, review)
            .orElseThrow(() -> new IllegalArgumentException("좋아요 정보가 존재하지 않습니다."));

        // 좋아요 삭제
        reviewLikeRepository.delete(reviewLike);

        // 리뷰 좋아요 수 감소
        review.decrementLikeCount();
        reviewRepository.save(review);
    }

    /**
     * 좋아요 여부 확인
     */
    public boolean isLiked(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        PerfactoReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        return reviewLikeRepository.existsByUserAndReview(user, review);
    }
}
