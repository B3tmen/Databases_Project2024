package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.OrderDAO;
import org.unibl.etf.database.dao.interfaces.OrderItemDAO;
import org.unibl.etf.database.dao.interfaces.OrderStatusDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.purchases.Order;
import org.unibl.etf.model.purchases.OrderStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private OrderStatusDAO orderStatusDAO = new OrderStatusDAOImpl();

    @Override
    public Order get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM bp_gamingshop.order";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int orderId = rs.getInt("order_id");
                BigDecimal grandTotal = rs.getBigDecimal("grand_total");
                BigDecimal shipping = rs.getBigDecimal("shipping");
                Date orderDate = rs.getDate("order_date");
                int orderStatusId = rs.getInt("fk_order_status_status_id");
                OrderStatus orderStatus = orderStatusDAO.get(orderStatusId);
                int customerId = rs.getInt("fk_customer_id");

                Order order = new Order(orderId, shipping, grandTotal, orderDate, orderStatus, customerId);
                order.setOrderItems(orderItemDAO.getAll(orderId));
                orders.add(order);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            //pstmt.close();
        }

        return orders;
    }

    @Override
    public int insert(Order object) throws SQLException {
        int orderId = 0;
        String sql = "CALL usp_AddOrder(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setBigDecimal(1, object.getShipping());
            cstmt.setDate(2, object.getOrderDate());
            cstmt.setInt(3, object.getOrderStatus().getId());
            cstmt.setInt(4, object.getCustomerId());

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                orderId = rs.getInt(1);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return orderId;
    }

    @Override
    public int update(Order object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Order object) throws SQLException {
        return 0;
    }

    @Override
    public void setOrderStatus(int orderId, int statusId) throws SQLException {
        String sql = "CALL usp_SetOrderStatus(?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, orderId);
            cstmt.setInt(2, statusId);
            cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            //cstmt.close();
        }
    }
}
