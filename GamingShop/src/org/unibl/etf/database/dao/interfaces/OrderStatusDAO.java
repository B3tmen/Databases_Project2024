package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.purchases.OrderStatus;

import java.sql.SQLException;

public interface OrderStatusDAO extends DAO<OrderStatus>{
    OrderStatus getOrderStatusByName(String status) throws SQLException;
}
