package org.unibl.etf.service;

import org.unibl.etf.database.dao.implementations.ProductCategoryDAOImpl;
import org.unibl.etf.database.dao.interfaces.ProductCategoryDAO;
import org.unibl.etf.model.product.ProductCategory;

import java.sql.SQLException;

public class ProductCategoryService {
    private ProductCategoryDAO productCategoryDAO;

    public ProductCategoryService(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public int addProductCategory(ProductCategory productCategory) throws SQLException {
        return productCategoryDAO.insert(productCategory);
    }
}
