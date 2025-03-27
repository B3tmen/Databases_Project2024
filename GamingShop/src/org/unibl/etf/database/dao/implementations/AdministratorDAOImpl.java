package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.AdministratorDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.users.Administrator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdministratorDAOImpl implements AdministratorDAO {


    @Override
    public Administrator get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Administrator> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Administrator object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddAdministrator(?, ?, ?, ?, ?, ?)";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, object.getUsername());
            cstmt.setString(2, object.getPasswordHash());
            cstmt.setString(3, object.getFirstName());
            cstmt.setString(4, object.getLastName());
            cstmt.setString(5, object.getEmail());
            cstmt.setBoolean(6, true);

            boolean hasResultSet = cstmt.execute();

            // Iterate through result sets, because usp_AddUser also generates a ResultSet with its user_id
            while (hasResultSet) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
                hasResultSet = cstmt.getMoreResults();
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return id;
    }

    @Override
    public int update(Administrator object) throws SQLException {
        int updatedAdministrators = 0;
        String sql = "CALL usp_UpdateAdministrator(?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, object.getId());
            cstmt.setString(2, object.getUsername());
            cstmt.setString(3, object.getPasswordHash());
            cstmt.setString(4, object.getFirstName());
            cstmt.setString(5, object.getLastName());
            cstmt.setString(6, object.getEmail());
            cstmt.setBoolean(7, object.isActive());

            updatedAdministrators = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return updatedAdministrators;
    }

    @Override
    public int delete(Administrator object) throws SQLException {
        return 0;
    }
}
