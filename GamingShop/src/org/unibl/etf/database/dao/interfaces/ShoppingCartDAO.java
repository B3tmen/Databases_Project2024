package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.purchases.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartDAO extends DAO<ShoppingCart>{
    boolean hasShoppingCart(int customerId) throws SQLException;
    ShoppingCart getShoppingCartByCustomerId(int customerId) throws SQLException;
}
