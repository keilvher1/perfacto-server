package org.example.scrd.dto;

import lombok.*;
import org.example.scrd.domain.ComparisonResult;
import org.example.scrd.domain.ReviewRating;
import org.example.scrd.domain.ReviewReason;

import java.util.Set;

/**
 * 리뷰 작성 요청 DTO (3단계 리뷰 시스템)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    private Long placeId; // 장소 ID

    // 1단계: 전체 평가
    private ReviewRating overallRating; // GOOD, NEUTRAL, BAD

    // 2단계: 이유 선택 (다중 선택)
    private Set<ReviewReason> reasons;

    // 3단계: 카테고리 비교 (선택적)
    private Long comparedPlaceId; // 비교 대상 장소 ID (null 가능)
    private ComparisonResult comparison; // BETTER, SIMILAR, WORSE (null 가능)
}
