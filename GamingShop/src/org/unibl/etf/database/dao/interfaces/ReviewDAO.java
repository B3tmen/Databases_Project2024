package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.product.Review;

import java.sql.SQLException;
import java.util.List;

public interface ReviewDAO extends DAO<Review>{
    Review getByProductId(int productId) throws SQLException;
    List<Review> getAll(int productId) throws SQLException;
}
