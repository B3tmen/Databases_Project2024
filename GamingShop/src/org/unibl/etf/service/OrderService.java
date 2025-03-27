package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.OrderDAO;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public Order getOrder(int id) throws SQLException {
        Order order = null;
        try{
            order = orderDAO.get(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return order;
    }

    public int addOrder(Order order){
        int addedOrders = 0;

        try{
            addedOrders = orderDAO.insert(order);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return addedOrders;
    }

    public List<Order> getOrders() {
        List<Order> orders = null;
        try{
            orders = orderDAO.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return orders;
    }

    public void setOrderStatus(int orderId, int statusId) {
        try{
            orderDAO.setOrderStatus(orderId, statusId);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
