package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.purchases.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDAO extends DAO<OrderItem>{
    List<OrderItem> getAll(int orderId) throws SQLException;
}
