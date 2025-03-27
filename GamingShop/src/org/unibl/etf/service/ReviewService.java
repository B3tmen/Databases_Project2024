package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ReviewDAO;
import org.unibl.etf.model.product.Review;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;

    public ReviewService(ReviewDAO reviewDAO){
        this.reviewDAO = reviewDAO;
    }

    public Review getByProductId(int productId){
        Review review = null;

        try{
            review = reviewDAO.getByProductId(productId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return review;
    }

    public List<Review> getAll(int productId){
        List<Review> reviews = null;

        try{
            reviews = reviewDAO.getAll(productId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return reviews;
    }

    public int addReview(Review review){
        int addedReviews = 0;
        try{
            addedReviews = reviewDAO.insert(review);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return addedReviews;
    }


}
