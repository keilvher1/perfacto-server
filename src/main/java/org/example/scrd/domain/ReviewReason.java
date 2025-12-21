package org.example.scrd.domain;

/**
 * 리뷰 이유/요소 선택지
 */
public enum ReviewReason {
    // 긍정적 요소 (GOOD)
    FOOD_DELICIOUS("음식이 맛있음", ReviewRating.GOOD),
    INTERIOR_NICE("인테리어가 예쁨", ReviewRating.GOOD),
    MUSIC_GOOD("나오는 노래가 좋음", ReviewRating.GOOD),
    SERVICE_EXCELLENT("서비스가 좋음", ReviewRating.GOOD),
    ATMOSPHERE_GOOD("분위기가 좋음", ReviewRating.GOOD),
    VALUE_FOR_MONEY("가성비가 좋음", ReviewRating.GOOD),
    WANT_TO_REVISIT("재방문 의사 있음", ReviewRating.GOOD),

    // 중립적 요소 (NEUTRAL)
    AVERAGE_QUALITY("무난함", ReviewRating.NEUTRAL),
    FAIR_PRICE("가격 대비 괜찮음", ReviewRating.NEUTRAL),
    NOTHING_SPECIAL("특별한 점 없음", ReviewRating.NEUTRAL),

    // 부정적 요소 (BAD)
    HYGIENE_POOR("위생이 더러움", ReviewRating.BAD),
    PARKING_LIMITED("주차공간이 협소함", ReviewRating.BAD),
    INTERIOR_UNAPPEALING("인테리어 디자인이 마음에 들지 않음", ReviewRating.BAD),
    SERVICE_UNFRIENDLY("서비스가 불친절함", ReviewRating.BAD),
    TOO_EXPENSIVE("가격이 비쌈", ReviewRating.BAD),
    LONG_WAIT_TIME("대기 시간이 김", ReviewRating.BAD),
    TOO_NOISY("시끄러움", ReviewRating.BAD);

    private final String description;
    private final ReviewRating associatedRating;

    ReviewReason(String description, ReviewRating associatedRating) {
        this.description = description;
        this.associatedRating = associatedRating;
    }

    public String getDescription() {
        return description;
    }

    public ReviewRating getAssociatedRating() {
        return associatedRating;
    }
}
