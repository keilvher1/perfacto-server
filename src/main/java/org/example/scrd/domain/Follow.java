package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 팔로우 관계 엔티티
 * 사용자 간의 팔로우 관계를 저장
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "follows",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_follower_following",
            columnNames = {"follower_id", "following_id"}
        )
    },
    indexes = {
        @Index(name = "idx_follower_id", columnList = "follower_id"),
        @Index(name = "idx_following_id", columnList = "following_id")
    }
)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 팔로우하는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 팔로우당하는 사람

    /**
     * 정적 팩토리 메서드
     */
    public static Follow create(User follower, User following) {
        return Follow.builder()
            .follower(follower)
            .following(following)
            .build();
    }
}
