package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ReceiptItemDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.purchases.ReceiptItem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ReceiptItemDAOImpl implements ReceiptItemDAO {
    @Override
    public ReceiptItem get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ReceiptItem> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(ReceiptItem object) throws SQLException {
        int addedCartItems = 0;
        String sql = "CALL usp_AddReceiptItem(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, object.getReceiptId());
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
    public int update(ReceiptItem object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(ReceiptItem object) throws SQLException {
        return 0;
    }
}
