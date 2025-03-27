package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.CategoryDAO;
import org.unibl.etf.model.product.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAll();
    }

    public Category getCategoryByProductId(int productId) throws SQLException {
        return categoryDAO.getCategoryByProductId(productId);
    }
}
