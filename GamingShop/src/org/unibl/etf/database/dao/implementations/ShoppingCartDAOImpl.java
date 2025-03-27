package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ShoppingCartDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.purchases.CartItem;
import org.unibl.etf.model.purchases.ShoppingCart;
import org.unibl.etf.service.CartItemService;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShoppingCartDAOImpl implements ShoppingCartDAO {

    @Override
    public ShoppingCart get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ShoppingCart> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(ShoppingCart object) throws SQLException {
        int insertedCarts = 0;
        String sql = "CALL usp_AddShoppingCart(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getCustomerId());

            insertedCarts = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return insertedCarts;
    }

    @Override
    public int update(ShoppingCart object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(ShoppingCart object) throws SQLException {
        return 0;
    }

    @Override
    public boolean hasShoppingCart(int customerId) throws SQLException {
        String sql = "CALL usp_HasShoppingCart(?, ?)";
        boolean hasCart = false;

        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, customerId);
            cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);

            cstmt.execute();
            hasCart = cstmt.getBoolean(2);
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return hasCart;
    }

    @Override
    public ShoppingCart getShoppingCartByCustomerId(int customerId) throws SQLException {
        ShoppingCart shoppingCart = null;
        String sql = "CALL usp_GetShoppingCartByCustomerId(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, customerId);

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                int cartId = rs.getInt("cart_id");
                shoppingCart = new ShoppingCart(cartId, customerId);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return shoppingCart;
    }
}
