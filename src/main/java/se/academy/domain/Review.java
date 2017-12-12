package se.academy.domain;

public class Review {
    private int productID;
    private int score;
    private String userReview;

    public Review() {
    }

    public Review(int productID, int score, String review) {
        this.productID = productID;
        this.score = score;
        this.userReview = review;
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

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }
}

