package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ReceiptDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.purchases.Receipt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReceiptDAOImpl implements ReceiptDAO {
    @Override
    public Receipt get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Receipt> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Receipt object) throws SQLException {
        int receiptId = 0;
        String sql = "CALL usp_AddReceipt(?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setTimestamp(1, object.getDateIssued());
            cstmt.setInt(2, object.getOrder().getId());
            cstmt.setBigDecimal(3, object.getGrandTotal());

            ResultSet rs = cstmt.executeQuery();
            if (rs.next()) {
                receiptId = rs.getInt(1);
                object.setId(receiptId);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return receiptId;
    }

    @Override
    public int update(Receipt object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Receipt object) throws SQLException {
        return 0;
    }
}
