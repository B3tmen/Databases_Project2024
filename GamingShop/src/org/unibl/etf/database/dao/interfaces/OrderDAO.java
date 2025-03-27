package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.purchases.Order;

import java.sql.SQLException;

public interface OrderDAO extends DAO<Order> {
    void setOrderStatus(int orderId, int statusId) throws SQLException;
}
