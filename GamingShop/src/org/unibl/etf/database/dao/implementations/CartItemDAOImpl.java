package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.CartItemDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.CartItem;
import org.unibl.etf.service.ProductService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {
    private ProductService productService = new ProductService(new ProductDAOImpl());

    @Override
    public CartItem get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<CartItem> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public List<CartItem> getAll(int cartId) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_item WHERE fk_shopping_cart_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cartId);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int productId = rs.getInt(2);
                Product product = productService.getProduct(productId);
                int quantity = rs.getInt(4);

                CartItem cartItem = new CartItem(cartId, product, quantity);
                cartItems.add(cartItem);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return cartItems;
    }

    @Override
    public int insert(CartItem object) throws SQLException {
        int addedCartItems = 0;
        String sql = "CALL usp_AddCartItem(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getShoppingCartId());
            cstmt.setInt(2, object.getProduct().getId());
            cstmt.setBigDecimal(3, object.getPrice());
            cstmt.setInt(4, object.getQuantity());

            addedCartItems =  cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return addedCartItems;
    }

    @Override
    public int update(CartItem object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(CartItem object) throws SQLException {
        String sql = "{CALL usp_DeleteCartItem(?, ?)}";
        int numberOfDeletions = 0;

        Connection conn = null;
        CallableStatement cstmt = null;
        try  {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getShoppingCartId());
            cstmt.setInt(2, object.getProduct().getId());

            numberOfDeletions = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return numberOfDeletions;
    }
}
