package org.unibl.etf.model.product;


import org.unibl.etf.model.enums.ReviewScoreEnum;

import java.math.BigDecimal;

public class Review {
    private int id;
    private String title;
    private ReviewScoreEnum score;
    private BigDecimal totalScore;
    private String comment;
    private int productId;
    private int customerId;

    public Review(int id, String title, ReviewScoreEnum score, BigDecimal totalScore, String comment, int productId, int customerId) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.totalScore = totalScore;
        this.comment = comment;
        this.productId = productId;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public ReviewScoreEnum getScore() {
        return score;
    }
    public String getComment() {
        return comment;
    }
    public int getProductId() {
        return productId;
    }
    public int getCustomerId() {
        return customerId;
    }


    public BigDecimal getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
}
