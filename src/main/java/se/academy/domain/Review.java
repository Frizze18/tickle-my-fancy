package se.academy.domain;

public class Review {
    private int reviewID;
    private int productID;
    private int score;
    private String review;

    public Review(int reviewID, int productID, int score, String review) {
        this.reviewID = reviewID;
        this.productID = productID;
        this.score = score;
        this.review = review;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

