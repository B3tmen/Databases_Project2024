package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ProductCategoryDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.product.ProductCategory;

import java.sql.*;
import java.util.List;

public class ProductCategoryDAOImpl implements ProductCategoryDAO {
    @Override
    public ProductCategory get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ProductCategory> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(ProductCategory object) throws SQLException {
        int productId = 0;
        String sql = "CALL usp_AddProductCategoryJunction(?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getProductId());
            cstmt.setInt(2, object.getCategoryId());
            cstmt.registerOutParameter(3, Types.INTEGER);

            cstmt.execute();
            productId = cstmt.getInt(3);

        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return productId;
    }

    @Override
    public int update(ProductCategory object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(ProductCategory object) {
        return 0;
    }
}
