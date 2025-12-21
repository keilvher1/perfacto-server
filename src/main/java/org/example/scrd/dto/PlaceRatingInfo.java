package org.example.scrd.dto;

import lombok.*;

import java.util.Map;

/**
 * 장소의 평점 정보 DTO (팔로우 vs 전체)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRatingInfo {

    private RatingDetail all; // 전체 평점
    private RatingDetail following; // 팔로우한 사람들의 평점

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingDetail {
        private Double average; // 평균 평점
        private Long count; // 리뷰 수
        private Map<String, Long> distribution; // 평점 분포 (GOOD, NEUTRAL, BAD 별 개수)
    }
}
