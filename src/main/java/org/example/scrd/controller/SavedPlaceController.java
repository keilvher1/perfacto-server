package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.Place;
import org.example.scrd.controller.response.ApiResponse;
import org.example.scrd.service.SavedPlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 저장된 장소 컨트롤러
 */
@RestController
@RequestMapping("/perfacto/api/saved-places")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SavedPlaceController {

    private final SavedPlaceService savedPlaceService;

    /**
     * 장소 저장
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> savePlace(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Long placeId = Long.parseLong(request.get("placeId").toString());
        String memo = (String) request.get("memo");
        savedPlaceService.savePlace(userId, placeId, memo);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 장소 저장 취소
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> unsavePlace(
            @PathVariable Long placeId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        savedPlaceService.unsavePlace(userId, placeId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * 저장된 장소 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Place>>> getSavedPlaces(
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<Place> places = savedPlaceService.getSavedPlaces(userId);
        return ResponseEntity.ok(ApiResponse.success(places));
    }

    /**
     * 저장 여부 확인
     */
    @GetMapping("/check/{placeId}")
    public ResponseEntity<ApiResponse<Boolean>> checkSaved(
            @PathVariable Long placeId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isSaved = savedPlaceService.isSaved(userId, placeId);
        return ResponseEntity.ok(ApiResponse.success(isSaved));
    }

    /**
     * 특정 사용자의 저장한 장소 + 리뷰 남긴 장소 조회 (중복 제거)
     * GET /api/saved-places/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Place>>> getUserPlaces(
            @PathVariable Long userId
    ) {
        List<Place> places = savedPlaceService.getUserPlaces(userId);
        return ResponseEntity.ok(ApiResponse.success(places));
    }
}
