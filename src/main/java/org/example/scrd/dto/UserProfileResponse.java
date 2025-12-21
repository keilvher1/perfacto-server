package org.example.scrd.dto;

import lombok.*;
import org.example.scrd.domain.User;

/**
 * 사용자 프로필 응답 DTO (소셜 기능 포함)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String profileImageUrl;

    // 소셜 정보
    private Integer followingCount;
    private Integer followerCount;
    private Integer reviewCount;
    private Boolean isFollowedByMe; // 현재 사용자가 팔로우 했는지

    // 맵 컬러링 정보
    private Integer visitedPolygonCount;

    /**
     * Entity to DTO
     */
    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .nickName(user.getNickName())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .followingCount(user.getFollowingCount())
            .followerCount(user.getFollowerCount())
            .reviewCount(user.getCount())
            .isFollowedByMe(false) // 별도로 설정 필요
            .visitedPolygonCount(user.getVisitedPolygons() != null ? user.getVisitedPolygons().size() : 0)
            .build();
    }

    public static UserProfileResponse from(User user, boolean isFollowedByMe) {
        UserProfileResponse response = from(user);
        response.setIsFollowedByMe(isFollowedByMe);
        return response;
    }
}
