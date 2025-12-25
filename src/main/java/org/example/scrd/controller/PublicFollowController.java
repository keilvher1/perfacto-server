package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.dto.UserProfileResponse;
import org.example.scrd.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public Follow Controller
 * 인증 없이 접근 가능한 팔로우 조회 API
 */
@RestController
@RequestMapping("/perfacto/every")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicFollowController {

    private final FollowService followService;

    /**
     * 특정 사용자의 팔로잉 목록 조회 (Public)
     */
    @GetMapping("/follows/{userId}/following")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getFollowingList(
            @PathVariable Long userId
    ) {
        List<UserProfileResponse> following = followService.getFollowingList(userId);
        return ResponseEntity.ok(ApiResponse.success(following));
    }

    /**
     * 특정 사용자의 팔로워 목록 조회 (Public)
     */
    @GetMapping("/follows/{userId}/followers")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getFollowerList(
            @PathVariable Long userId
    ) {
        List<UserProfileResponse> followers = followService.getFollowerList(userId);
        return ResponseEntity.ok(ApiResponse.success(followers));
    }
}
