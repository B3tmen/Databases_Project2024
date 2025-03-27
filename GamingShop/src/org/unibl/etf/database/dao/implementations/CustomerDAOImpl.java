package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.CustomerDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.database.mysql.DatabaseConnection;
import org.unibl.etf.model.users.Customer;

import java.sql.*;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Customer get(int id) throws SQLException {
        Customer customer = null;
        String sql = "CALL usp_GetCustomerById(?)";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setInt(1, id);

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                int customerId = rs.getInt(1);
                String username = rs.getString(2);
                String passwordHash = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                String email = rs.getString(6);
                boolean isActive = rs.getBoolean(7);
                String phoneNumber = rs.getString(8);
                int addressId = rs.getInt(9);

                customer = new Customer(customerId, username, passwordHash, firstName, lastName, email, isActive, phoneNumber, addressId);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return customer;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Customer object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddCustomer(?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, object.getUsername());
            cstmt.setString(2, object.getPasswordHash());
            cstmt.setString(3, object.getFirstName());
            cstmt.setString(4, object.getLastName());
            cstmt.setString(5, object.getEmail());
            cstmt.setBoolean(6, true);
            cstmt.setInt(7, object.getAdressId());
            cstmt.setString(8, object.getPhoneNumber());

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
    public int update(Customer object) throws SQLException {
        int updatedCustomers = 0;
        String sql = "CALL usp_UpdateCustomer(?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            cstmt.setString(8, object.getPhoneNumber());
            cstmt.setInt(9, object.getAdressId());

            updatedCustomers = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return updatedCustomers;
    }

    @Override
    public int delete(Customer object) {
        return 0;
    }

    @Override
    public int registerCustomer(Customer customer) throws SQLException{
        String sql = "CALL usp_RegisterCustomer(?, ?, ?, ?, ?, ?, ?)";
        int id = -1;
        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, customer.getUsername());
            cstmt.setString(2, customer.getPasswordHash());
            cstmt.setString(3, customer.getFirstName());
            cstmt.setString(4, customer.getLastName());
            cstmt.setString(5, customer.getEmail());
            cstmt.setString(6, customer.getPhoneNumber());
            cstmt.setInt(7, customer.getAdressId());

            ResultSet rs = cstmt.executeQuery();

            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return id;
    }

    @Override
    public int getLastCustomerId() throws SQLException {
        int id = -1;
        String sql = "{CALL usp_GetLastCustomerID(?)}";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.registerOutParameter(1, Types.INTEGER);

            cstmt.execute();

            id = cstmt.getInt(1);
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return id;
    }
}
