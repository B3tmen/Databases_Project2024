package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.OrderItemDAO;
import org.unibl.etf.model.purchases.OrderItem;

import java.sql.SQLException;
import java.util.List;

public class OrderItemService {
    private OrderItemDAO orderItemDAO;

    public OrderItemService(OrderItemDAO orderItemDAO) {
        this.orderItemDAO = orderItemDAO;
    }

    public int addOrderItem(OrderItem orderItem) {
        int placedItems = 0;
        try{
            placedItems = orderItemDAO.insert(orderItem);
        } catch(SQLException e){
            e.printStackTrace();
        }

        return placedItems;
    }

    public List<OrderItem> getOrderItemsById(int orderId) {
        List<OrderItem> orderItems = null;
        try{
            orderItems = orderItemDAO.getAll(orderId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return orderItems;
    }
}
