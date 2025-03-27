package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.users.Customer;

import java.sql.SQLException;

public interface CustomerDAO extends DAO<Customer> {
    int registerCustomer(Customer customer) throws SQLException;
    int getLastCustomerId() throws SQLException;
}
