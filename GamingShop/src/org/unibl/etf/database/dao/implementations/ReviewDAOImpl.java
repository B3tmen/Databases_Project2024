package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ReviewDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.enums.ReviewScoreEnum;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.product.Review;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {
    @Override
    public Review get(int id) throws SQLException {
        return null;
    }

    @Override
    public Review getByProductId(int productId) throws SQLException {
        String sql = "SELECT * FROM review WHERE fk_product_id = ?";
        Review review = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String score = rs.getString(3);
                BigDecimal totalScore = rs.getBigDecimal(4);
                String comment = rs.getString(5);
                int customerId = rs.getInt(7);
                ReviewScoreEnum reviewScoreEnum = ReviewScoreEnum.ZERO;
                switch (score){
                    case "1" -> reviewScoreEnum = ReviewScoreEnum.ONE;
                    case "2" -> reviewScoreEnum = ReviewScoreEnum.TWO;
                    case "3" -> reviewScoreEnum = ReviewScoreEnum.THREE;
                    case "4" -> reviewScoreEnum = ReviewScoreEnum.FOUR;
                    case "5" -> reviewScoreEnum = ReviewScoreEnum.FIVE;
                }

                review = new Review(id, title, reviewScoreEnum, totalScore, comment, productId, customerId);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return review;
    }


    @Override
    public List<Review> getAll() throws SQLException {
        return List.of();
    }


    @Override
    public List<Review> getAll(int productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT review_id, title, score, total_score, comment, fk_customer_id FROM review WHERE fk_product_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareCall(sql);
            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String score = rs.getString(3);
                BigDecimal totalScore = rs.getBigDecimal(4);
                String comment = rs.getString(5);
                int customerId = rs.getInt(6);
                ReviewScoreEnum reviewScoreEnum = ReviewScoreEnum.ZERO;
                switch (score){
                    case "1" -> reviewScoreEnum = ReviewScoreEnum.ONE;
                    case "2" -> reviewScoreEnum = ReviewScoreEnum.TWO;
                    case "3" -> reviewScoreEnum = ReviewScoreEnum.THREE;
                    case "4" -> reviewScoreEnum = ReviewScoreEnum.FOUR;
                    case "5" -> reviewScoreEnum = ReviewScoreEnum.FIVE;
                }

                Review review = new Review(id, title, reviewScoreEnum, totalScore, comment, productId, customerId);
                reviews.add(review);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return reviews;
    }

    @Override
    public int insert(Review object) throws SQLException {
        int addedReviews = 0;
        String sql = "CALL usp_AddReview(?, ?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, object.getTitle());
            cstmt.setString(2, object.getScore().getReviewScore());
            cstmt.setString(3, object.getComment());
            cstmt.setInt(4, object.getProductId());
            cstmt.setInt(5, object.getCustomerId());

            addedReviews = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return addedReviews;
    }

    @Override
    public int update(Review object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Review object) throws SQLException {
        return 0;
    }

}
