package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.purchases.CartItem;

import java.sql.SQLException;
import java.util.List;

public interface CartItemDAO extends DAO<CartItem> {
    List<CartItem> getAll(int cartId) throws SQLException;
}
