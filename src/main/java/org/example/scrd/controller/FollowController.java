package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.dto.UserProfileResponse;
import org.example.scrd.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 팔로우 컨트롤러
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Void> follow(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long currentUserId = Long.parseLong(authentication.getName());
        followService.follow(currentUserId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 언팔로우
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unfollow(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long currentUserId = Long.parseLong(authentication.getName());
        followService.unfollow(currentUserId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 팔로잉 목록 조회
     */
    @GetMapping("/following")
    public ResponseEntity<List<UserProfileResponse>> getFollowingList(
            Authentication authentication
    ) {
        Long currentUserId = Long.parseLong(authentication.getName());
        List<UserProfileResponse> following = followService.getFollowingList(currentUserId);
        return ResponseEntity.ok(following);
    }

    /**
     * 팔로워 목록 조회
     */
    @GetMapping("/followers")
    public ResponseEntity<List<UserProfileResponse>> getFollowerList(
            Authentication authentication
    ) {
        Long currentUserId = Long.parseLong(authentication.getName());
        List<UserProfileResponse> followers = followService.getFollowerList(currentUserId);
        return ResponseEntity.ok(followers);
    }

    /**
     * 팔로우 여부 확인
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkFollow(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long currentUserId = Long.parseLong(authentication.getName());
        boolean isFollowing = followService.isFollowing(currentUserId, userId);
        return ResponseEntity.ok(isFollowing);
    }
}
