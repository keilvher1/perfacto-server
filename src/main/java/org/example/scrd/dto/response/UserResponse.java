package org.example.scrd.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.scrd.domain.User;
import org.example.scrd.dto.Tier;

@NoArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String profileImageUrl;
    private Tier tier;
    private String gender;
    private String birth;
    private int point;
    private int count;
    private Integer followingCount;
    private Integer followerCount;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.nickName = user.getNickName();
        response.email = user.getEmail();
        response.profileImageUrl = user.getProfileImageUrl();
        response.tier = user.getTier();
        response.gender = user.getGender();
        response.birth = user.getBirth();
        response.point = user.getPoint();
        response.count = user.getCount();
        response.followingCount = user.getFollowingCount();
        response.followerCount = user.getFollowerCount();
        return response;
    }
}
