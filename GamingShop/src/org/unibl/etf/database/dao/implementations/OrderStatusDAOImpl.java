package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.OrderStatusDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.enums.OrderStatusEnum;
import org.unibl.etf.model.purchases.OrderStatus;

import java.sql.*;
import java.util.List;

public class OrderStatusDAOImpl implements OrderStatusDAO {

    @Override
    public OrderStatus get(int id) throws SQLException {
        OrderStatus orderStatus = null;
        String query = "SELECT * FROM order_status WHERE status_id = ?;";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                orderStatus = new OrderStatus(
                        rs.getInt("status_id"),
                        OrderStatusEnum.valueOf(rs.getString("status").toUpperCase())
                );
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return orderStatus;
    }

    @Override
    public List<OrderStatus> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(OrderStatus object) throws SQLException {
        int insertedStatuses = 0;
        String sql = "CALL usp_AddOrderStatus(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, object.getOrderStatusEnum().getStatus());

            insertedStatuses = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return insertedStatuses;
    }

    @Override
    public int update(OrderStatus object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(OrderStatus object) throws SQLException {
        return 0;
    }

    @Override
    public OrderStatus getOrderStatusByName(String status) throws SQLException {
        OrderStatus orderStatus = null;
        String sql = "CALL GetOrderStatusByName(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, status);
            ResultSet rs = cstmt.executeQuery(sql);

            if(rs.next()) {
                orderStatus = new OrderStatus(
                        rs.getInt("status_id"),
                        OrderStatusEnum.valueOf(rs.getString("status"))
                );
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return orderStatus;
    }
}
