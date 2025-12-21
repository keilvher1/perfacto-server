package org.example.scrd.domain;

/**
 * 카테고리 비교 결과
 */
public enum ComparisonResult {
    BETTER("이곳이 더 좋았음"),
    SIMILAR("비슷했음"),
    WORSE("저곳이 더 좋았음");

    private final String description;

    ComparisonResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
