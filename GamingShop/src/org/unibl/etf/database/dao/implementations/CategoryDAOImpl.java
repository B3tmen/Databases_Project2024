package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.CategoryDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.database.mysql.DatabaseConnection;
import org.unibl.etf.model.product.Category;
import org.unibl.etf.model.product.Manufacturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public Category get(int id) throws SQLException {
        Category category = null;
        String sql = "SELECT * FROM category WHERE category_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getInt("fk_parent_category_id")
                );
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
        }

        return category;
    }

    @Override
    public List<Category> getAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getInt("fk_parent_category_id")
                );

                categories.add(category);
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
        }

        return categories;
    }

    @Override
    public int insert(Category object) throws SQLException {
        return 0;
    }

    @Override
    public int update(Category object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Category object) {
        return 0;
    }

    @Override
    public Category getCategoryByProductId(int id) throws SQLException {
        Category category = null;
        String sql = "CALL usp_GetCategoryByProductId(?)";

        Connection connection = null;
        PreparedStatement pstmt = null;
        try{
            connection = ConnectionPool.getInstance().checkOut();
            CallableStatement cstmt = connection.prepareCall(sql);

            cstmt.setInt(1, id);

            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {
                category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getInt("fk_parent_category_id")
                );
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(connection);
        }

        return category;
    }
}
