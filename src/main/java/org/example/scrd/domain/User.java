package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;
import org.example.scrd.dto.Tier;
import org.example.scrd.dto.UserDto;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;

    @Column(name = "apple_id")
    private String appleId;

    @Column(name = "naver_id")
    private String naverId;

    @Setter
    @Column(columnDefinition = "varchar(200)")
    private String name;

    @Setter
    @Column(columnDefinition = "varchar(30)")
    private String email;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Tier tier;
    private String gender;
    private String birth;

    @Setter
    @Column(columnDefinition = "varchar(30)")
    private String nickName;

    @Setter
    @Column(columnDefinition = "varchar(255)")
    private String password; // 자체 회원가입용 암호화된 비밀번호

    @Setter
    private int point; // 결제 시스템 적용 후 사용되는 포인트 review 작성시 +500
    @Setter
    private int count; // review 쓸때마다 +1

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    public void setCount(int count) {
        this.count = count;
        this.tier = Tier.getTierByCount(count);
    }

    @OneToMany(mappedBy = "user")
    private List<SavedTheme> likes = new ArrayList<>();

    // 소셜 기능 추가
    @Setter
    @Builder.Default
    @Column(nullable = false)
    private Integer followingCount = 0; // 팔로잉 수

    @Setter
    @Builder.Default
    @Column(nullable = false)
    private Integer followerCount = 0; // 팔로워 수

    // 방문한 폴리곤 ID 목록 (맵 컬러링용, 콤마로 구분하여 저장)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_visited_polygons", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "polygon_id")
    @Builder.Default
    private Set<Long> visitedPolygons = new HashSet<>();

    // TODO: 추후 성별, 생일 받으면 빌더 타입 수정해야함.
    public static User from(UserDto dto){
        return User.builder()
                .kakaoId(dto.getKakaoId())
                .appleId(dto.getAppleId())
                .naverId(dto.getNaverId())
                .name(dto.getName())
                .email(dto.getEmail())
                .profileImageUrl(dto.getProfileImageUrl())
                .role(Role.ROLE_USER) // 기본 권한 설정
                .followingCount(0)
                .followerCount(0)
                .build();
    }
    public static User addReviewFrom(User user) {
        user.setPoint(user.getPoint() + 500); // 포인트 500 증가
        user.setCount(user.getCount() + 1);   // 리뷰 횟수 1 증가
        return user;
    }

    // 팔로잉 수 증가
    public void incrementFollowingCount() {
        this.followingCount++;
    }

    // 팔로잉 수 감소
    public void decrementFollowingCount() {
        if (this.followingCount > 0) {
            this.followingCount--;
        }
    }

    // 팔로워 수 증가
    public void incrementFollowerCount() {
        this.followerCount++;
    }

    // 팔로워 수 감소
    public void decrementFollowerCount() {
        if (this.followerCount > 0) {
            this.followerCount--;
        }
    }

    // 방문한 폴리곤 추가
    public void addVisitedPolygon(Long polygonId) {
        if (this.visitedPolygons == null) {
            this.visitedPolygons = new HashSet<>();
        }
        this.visitedPolygons.add(polygonId);
    }

}
