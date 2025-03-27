package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.EmployeeDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public Employee get(int id) throws SQLException {
        Employee employee = null;
        String sql = "CALL usp_GetEmployeeById(?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
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
                String position = rs.getString(8);
                BigDecimal salary = rs.getBigDecimal(9);

                employee = new Employee(customerId, username, passwordHash, firstName, lastName, email, isActive, position, salary);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return employee;
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Employee object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddEmployee(?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, object.getUsername());
            cstmt.setString(2, object.getPasswordHash());
            cstmt.setString(3, object.getFirstName());
            cstmt.setString(4, object.getLastName());
            cstmt.setString(5, object.getEmail());
            cstmt.setBoolean(6, true);
            cstmt.setString(7, object.getPosition());
            cstmt.setBigDecimal(8, object.getSalary());

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
    public int update(Employee object) throws SQLException {
        int updatedEmployees = 0;
        String sql = "CALL usp_UpdateEmployee(?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            cstmt.setString(8, object.getPosition());
            cstmt.setBigDecimal(9, object.getSalary());

            updatedEmployees = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return updatedEmployees;
    }

    @Override
    public int delete(Employee object) throws SQLException {
        return 0;
    }

    @Override
    public Employee getEmployeeById(int id) {
        return null;
    }
}
