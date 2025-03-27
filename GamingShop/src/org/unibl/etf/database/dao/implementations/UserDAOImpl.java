package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.UserDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.CustomerService;
import org.unibl.etf.service.EmployeeService;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private CustomerService customerService = new CustomerService(new CustomerDAOImpl());
    private EmployeeService employeeService = new EmployeeService(new EmployeeDAOImpl());

    public UserDAOImpl() {

    }

    @Override
    public User get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password_hash");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String userType = resultSet.getString("type_");
                boolean isActive = resultSet.getBoolean("is_active");

                if("Administrator".equals(userType)) {
                    users.add(new Administrator(id, username, passwordHash, firstName, lastName, email, isActive));
                }
                else if("Employee".equals(userType)){
                    Employee employee = new Employee(id, username, passwordHash, firstName, lastName, email, isActive, "", BigDecimal.valueOf(0));
                    getMissingEmployeeDetails(employee);

                    users.add(employee);
                }
                else{
                    Customer customer = new Customer(id, username, passwordHash, firstName, lastName, email, isActive, "", 0);
                    getMissingCustomerDetails(customer);

                    users.add(customer);
                }
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return users;
    }

    @Override
    public int insert(User object) throws SQLException {
        return 0;
    }

    @Override
    public int update(User object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(User object) {
        return 0;
    }

    @Override
    public User getByUsernameAndPassword(String username, String passwordHash) throws SQLException {
        String sql = "CALL usp_GetUserByUsernameAndPassword(?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, username);
            cstmt.setString(2, passwordHash);
            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {
                String type = rs.getString("type_");

                switch (type) {
                    case "Customer":
                        return new Customer(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                                rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
                                rs.getBoolean("is_active"), rs.getString("phone_number"), rs.getInt("fk_address_id"));
                    case "Administrator":
                        return new Administrator(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                                rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
                                rs.getBoolean("is_active"));
                    case "Employee":
                        return new Employee(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                                rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
                                rs.getBoolean("is_active"), rs.getString("position"), rs.getBigDecimal("salary"));
                }
            }
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }
        return null;
    }

    @Override
    public int setUserActivationStatus(int userId, boolean activationStatus) throws SQLException {
        int affectedUsers = 0;
        String sql = "CALL usp_SetUserActivationStatus(?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, userId);
            cstmt.setBoolean(2, activationStatus);

            affectedUsers = cstmt.executeUpdate();
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return affectedUsers;
    }

    private void getMissingEmployeeDetails(Employee employee){
        Employee employeeHelp = employeeService.getEmployeeById(employee.getId());

        employee.setPosition(employeeHelp.getPosition());
        employee.setSalary(employeeHelp.getSalary());
    }

    private void getMissingCustomerDetails(Customer customer){
        Customer customerHelp = customerService.getCustomerById(customer.getId());

        customer.setPhoneNumber(customerHelp.getPhoneNumber());
        customer.setAdressId(customerHelp.getAdressId());
    }
}