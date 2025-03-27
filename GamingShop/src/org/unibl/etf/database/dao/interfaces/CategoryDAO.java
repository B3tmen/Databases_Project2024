package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.product.Category;

import java.sql.SQLException;

public interface CategoryDAO extends DAO<Category> {
    Category getCategoryByProductId(int id) throws SQLException;
}
