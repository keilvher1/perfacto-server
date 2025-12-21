package org.example.scrd.domain;

/**
 * 리뷰 전체 평가 (신호등 방식)
 */
public enum ReviewRating {
    GOOD("좋았음"),      // 긍정
    NEUTRAL("보통"),     // 중립
    BAD("별로임");       // 부정

    private final String description;

    ReviewRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 숫자 값으로 변환 (평균 계산용)
     */
    public int getNumericValue() {
        switch (this) {
            case GOOD: return 5;
            case NEUTRAL: return 3;
            case BAD: return 1;
            default: return 3;
        }
    }
}
