package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ShoppingCartDAO;
import org.unibl.etf.model.purchases.ShoppingCart;

import java.sql.SQLException;

public class ShoppingCartService {
    private ShoppingCartDAO shoppingCartDAO;

    public ShoppingCartService(ShoppingCartDAO shoppingCartDAO) {
        this.shoppingCartDAO = shoppingCartDAO;
    }

    public int addShoppingCart(ShoppingCart shoppingCart) {
        int addedCarts = 0;
        try{
            addedCarts = shoppingCartDAO.insert(shoppingCart);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return addedCarts;
    }

    public boolean customerHasShoppingCart(int customerId) {
        boolean hasCart = false;
        try{
            hasCart = shoppingCartDAO.hasShoppingCart(customerId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return hasCart;
    }

    public ShoppingCart getShoppingCartByCustomerId(int customerId) {
        ShoppingCart cart = null;
        try{
            cart = shoppingCartDAO.getShoppingCartByCustomerId(customerId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return cart;
    }
}
