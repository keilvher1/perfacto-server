package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 리뷰 좋아요 엔티티
 * 사용자가 리뷰에 좋아요를 표시
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "review_likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_review",
            columnNames = {"user_id", "review_id"}
        )
    },
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_review_id", columnList = "review_id")
    }
)
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 좋아요를 누른 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private PerfactoReview review; // 좋아요 대상 리뷰

    /**
     * 정적 팩토리 메서드
     */
    public static ReviewLike create(User user, PerfactoReview review) {
        return ReviewLike.builder()
            .user(user)
            .review(review)
            .build();
    }
}
