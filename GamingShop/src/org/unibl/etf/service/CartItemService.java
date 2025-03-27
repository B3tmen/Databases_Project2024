package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.CartItemDAO;
import org.unibl.etf.model.purchases.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartItemService {
    private CartItemDAO cartItemDAO;

    public CartItemService(CartItemDAO cartItemDAO) {
        this.cartItemDAO = cartItemDAO;
    }

    public int addCartItem(CartItem cartItem) {
        int addedCartItems = 0;
        try{
            addedCartItems = cartItemDAO.insert(cartItem);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return addedCartItems;
    }

    public List<CartItem> getAllCartItems(int cartId) {
        List<CartItem> cartItems = null;
        try{
            cartItems = cartItemDAO.getAll(cartId);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return cartItems;
    }

    public int deleteCartItem(CartItem item) {
        int deletedCartItems = 0;
        try{
            deletedCartItems = cartItemDAO.delete(item);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return deletedCartItems;
    }
}
