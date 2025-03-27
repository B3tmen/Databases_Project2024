package org.unibl.etf.model.enums;

public enum ReviewScoreEnum {
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private final String reviewScore;

    ReviewScoreEnum(String reviewScore) {
        this.reviewScore = reviewScore;
    }

    public String getReviewScore() {
        return reviewScore;
    }
}
