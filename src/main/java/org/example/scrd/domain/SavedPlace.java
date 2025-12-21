package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 저장된 장소 엔티티
 * 사용자가 나중에 방문하기 위해 저장한 장소
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "saved_places",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_place",
            columnNames = {"user_id", "place_id"}
        )
    },
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_place_id", columnList = "place_id")
    }
)
public class SavedPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 저장한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 저장된 장소

    @Column(columnDefinition = "TEXT")
    private String memo; // 저장 이유 메모 (선택)

    /**
     * 정적 팩토리 메서드
     */
    public static SavedPlace create(User user, Place place, String memo) {
        return SavedPlace.builder()
            .user(user)
            .place(place)
            .memo(memo)
            .build();
    }
}
