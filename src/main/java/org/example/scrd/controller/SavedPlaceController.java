package org.example.scrd.controller;

import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.Place;
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
@RequestMapping("/api/saved-places")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SavedPlaceController {

    private final SavedPlaceService savedPlaceService;

    /**
     * 장소 저장
     */
    @PostMapping
    public ResponseEntity<Void> savePlace(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Long placeId = Long.parseLong(request.get("placeId").toString());
        String memo = (String) request.get("memo");
        savedPlaceService.savePlace(userId, placeId, memo);
        return ResponseEntity.ok().build();
    }

    /**
     * 장소 저장 취소
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> unsavePlace(
            @PathVariable Long placeId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        savedPlaceService.unsavePlace(userId, placeId);
        return ResponseEntity.ok().build();
    }

    /**
     * 저장된 장소 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<Place>> getSavedPlaces(
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        List<Place> places = savedPlaceService.getSavedPlaces(userId);
        return ResponseEntity.ok(places);
    }

    /**
     * 저장 여부 확인
     */
    @GetMapping("/check/{placeId}")
    public ResponseEntity<Boolean> checkSaved(
            @PathVariable Long placeId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isSaved = savedPlaceService.isSaved(userId, placeId);
        return ResponseEntity.ok(isSaved);
    }
}
