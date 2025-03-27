package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.OrderItemDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.OrderItem;
import org.unibl.etf.service.ProductService;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {
    private ProductService productService = new ProductService(new ProductDAOImpl());

    @Override
    public OrderItem get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OrderItem> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public List<OrderItem> getAll(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "CALL usp_GetOrderItemsByOrderId(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, orderId);

            ResultSet rs = cstmt.executeQuery();
            while (rs.next()){
                int productId = rs.getInt("fk_product_id");
                Product product = productService.getProduct(productId);
                int quantity = rs.getInt("quantity");

                OrderItem orderItem = new OrderItem(orderId, product, quantity);
                orderItems.add(orderItem);
            }
        }
        finally {
            ConnectionPool.getInstance().checkOut();
            cstmt.close();
        }

        return orderItems;
    }

    @Override
    public int insert(OrderItem object) throws SQLException {
        int insertedOrderItems = 0;
        String sql = "CALL usp_AddOrderItem(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getOrderId());
            cstmt.setInt(2, object.getProduct().getId());
            cstmt.setBigDecimal(3, object.getPrice());
            cstmt.setInt(4, object.getQuantity());

            insertedOrderItems = cstmt.executeUpdate();
        }
        catch(SQLException exception){
            exception.printStackTrace();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return insertedOrderItems;
    }

    @Override
    public int update(OrderItem object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(OrderItem object) throws SQLException {
        return 0;
    }


}
