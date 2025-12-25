package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.domain.User;
import org.example.scrd.dto.request.PartyJoinRequest;
import org.example.scrd.dto.request.UserProfileUpdateRequest;
import org.example.scrd.dto.response.UserResponse;
import org.example.scrd.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfacto/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserResponse userResponse = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(userResponse);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long targetId
    ) {
        userService.deleteUser(currentUser, targetId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Object>>updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserProfileUpdateRequest request) {
        userService.updateUserProfile(user, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 사용자 검색 (닉네임 또는 이름으로 검색)
     * GET /perfacto/api/user/search?query={검색어}
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
            @RequestParam String query) {
        List<UserResponse> users = userService.searchUsers(query);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * 특정 사용자 정보 조회
     * GET /perfacto/api/user/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

}
