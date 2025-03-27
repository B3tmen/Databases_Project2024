package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.DiscountDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.Discount;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DiscountDAOImpl implements DiscountDAO {


    @Override
    public Discount get(int id) throws SQLException {
        String sql = "SELECT * FROM discount WHERE discount_id = ?";
        Discount discount = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int discountId = rs.getInt(1);
                BigDecimal percentage = rs.getBigDecimal(2);
                Date dateFrom = rs.getDate(3);
                Date dateTo = rs.getDate(4);

                discount = new Discount(discountId, percentage, dateFrom, dateTo ,0);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return discount;
    }

    @Override
    public List<Discount> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Discount object) throws SQLException {
        int discountId = 0;
        String sql = "CALL usp_AddDiscount(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setBigDecimal(1, object.getPercentage());
            cstmt.setDate(2, object.getDateFrom());
            cstmt.setDate(3, object.getDateTo());
            cstmt.setInt(4, object.getEmployeeId());

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                discountId = rs.getInt(1);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return discountId;
    }

    @Override
    public int update(Discount object) throws SQLException {
        int updatedDiscounts = 0;
        String sql = "CALL usp_UpdateDiscount(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try  {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getId());
            cstmt.setBigDecimal(2, object.getPercentage());
            cstmt.setDate(3, object.getDateFrom());
            cstmt.setDate(4, object.getDateTo());


            updatedDiscounts = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return updatedDiscounts;
    }

    @Override
    public int delete(Discount object) throws SQLException {
        return 0;
    }
}
