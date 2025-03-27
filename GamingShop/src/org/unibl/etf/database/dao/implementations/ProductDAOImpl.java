package org.unibl.etf.database.dao.implementations;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.unibl.etf.database.dao.interfaces.ProductDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.database.mysql.DatabaseConnection;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.util.ImagePaths;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    // region CRUD operations
    @Override
    public Product get(int id) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        Product product = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                product = getProductFromResultSet(rs);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return product;
    }

    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        return getProducts(products, sql);
    }

    @Override
    public int insert(Product object) throws SQLException {
        int productId = 0;
        String sql = "CALL usp_AddProduct(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setString(1, object.getName());
            cstmt.setBigDecimal(2, object.getPrice());
            cstmt.setInt(3, object.getQuantityAvailable());
            cstmt.setString(4, object.getDescription());
            cstmt.setInt(5, object.getWarrantyMonths());
            if(object.getImageUrl() != null){
                cstmt.setString(6, object.getImageUrl());
            } else{
                cstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            if(object.getDiscountId() != 0){
                cstmt.setInt(7, object.getDiscountId());
            } else {
                cstmt.setNull(7, java.sql.Types.INTEGER);
            }
            cstmt.setInt(8, object.getEmployeeId());
            cstmt.setInt(9, object.getManufacturerId());

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                productId = rs.getInt(1);
            }

        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return productId;
    }

    @Override
    public int update(Product object) throws SQLException {
        String sql = "CALL usp_UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int updatedProducts = 0;

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getId());
            cstmt.setString(2, object.getName());
            cstmt.setBigDecimal(3, object.getPrice());
            cstmt.setInt(4, object.getQuantityAvailable());
            cstmt.setString(5, object.getDescription());
            cstmt.setInt(6, object.getWarrantyMonths());
            cstmt.setString(7, object.getImageUrl());
            if(object.getDiscountId() != 0){
                cstmt.setInt(8, object.getDiscountId());
            }
            else{
                cstmt.setNull(8, java.sql.Types.INTEGER);
            }
            cstmt.setInt(9, object.getEmployeeId());
            cstmt.setInt(10, object.getManufacturerId());

            updatedProducts = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return updatedProducts;
    }

    @Override
    public int delete(Product object) throws SQLException {
        String sql = "{CALL usp_DeleteProductById(?)}";
        int id = -1;

        Connection conn = null;
        CallableStatement cstmt = null;
        try  {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getId());

            id = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return id;
    }
    // endregion


    // region Getting products by categories methods

    @Override
    public List<Product> getPcComponentsProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_pc_components_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getCpuProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_cpu_products";

        return getProducts(products, sql);
    }


    @Override
    public List<Product> getGpuProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_gpu_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getRamProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_ram_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getStorageProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_storage_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getMotherboardProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_motherboard_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getPcPeripheralsProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_pc_peripherals_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getKeyboardProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_keyboard_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getMiceProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_mice_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getHeadphonesProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_headphones_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getConsolesProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_consoles_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getPlaystationProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_playstation_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getXBoxProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_xbox_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getGamesProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_games_products";

        return getProducts(products, sql);
    }

    @Override
    public List<Product> getCablesAndAdaptersProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM vw_cables_products";

        return getProducts(products, sql);
    }

    // endregion


    // region Helper methods
    private Product getProductFromResultSet(ResultSet rs) throws SQLException {
        int productId = rs.getInt(1);
        String name = rs.getString(2);
        BigDecimal price = rs.getBigDecimal(3);
        BigDecimal discountedPrice = rs.getBigDecimal(4);
        int quantity = rs.getInt(5);
        String description = rs.getString(6);
        int warranty = rs.getInt(7);
        String imageUrl = rs.getString(8);
        int discountId = rs.getInt(9);
        int employeeId = rs.getInt(10);
        int manufacturerId = rs.getInt(11);

        Product product = new Product(productId, name, price, discountedPrice, quantity, description, warranty, imageUrl, discountId, employeeId, manufacturerId);
        product.setCategoryName();

        return product;
    }

    private List<Product> getProducts(List<Product> products, String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Product product = getProductFromResultSet(resultSet);

                products.add(product);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return products;
    }

    // endregion
}
