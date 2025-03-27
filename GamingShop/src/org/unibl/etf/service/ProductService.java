package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ProductDAO;
import org.unibl.etf.model.product.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // region CRUD operations

    public Product getProduct(int id) throws SQLException {
        return productDAO.get(id);
    }

    public List<Product> getProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public int addProduct(Product product) throws SQLException {
        return productDAO.insert(product);
    }

    public int updateProduct(Product product) throws SQLException {
        return productDAO.update(product);
    }

    public int deleteProduct(Product product) throws SQLException {
        return productDAO.delete(product);
    }

    // endregion


    // region Getting products by categories methods

    public List<Product> getPcComponentsProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getPcComponentsProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getCpuProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getCpuProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getGpuProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getGpuProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getRamProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getRamProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getStorageProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getStorageProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getMotherboardProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getMotherboardProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


    public List<Product> getPcPeripheralsProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getPcPeripheralsProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getKeyboardProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getKeyboardProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getMiceProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getMiceProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getHeadphonesProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getHeadphonesProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getConsolesProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getConsolesProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getPlaystationProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getPlaystationProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getXboxProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getXBoxProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getGamesProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getGamesProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getCablesAndAdaptersProducts() {
        List<Product> products = null;
        try {
            products = productDAO.getCablesAndAdaptersProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // enderegion

}
