package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.CustomerDAO;
import org.unibl.etf.model.users.Customer;

import java.sql.SQLException;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer getCustomerById(int id){
        Customer customer = null;
        try{
            customer = customerDAO.get(id);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return customer;
    }

    public int addCustomer(Customer customer){
        int id = 0;
        try {
            id = customerDAO.insert(customer);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }

    public int updateCustomer(Customer customer){
        int updatedCustomers = 0;
        try {
            updatedCustomers = customerDAO.update(customer);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return updatedCustomers;
    }

    public int registerCustomer(Customer customer){
        int customerId = 0;
        try {
            customerId = customerDAO.registerCustomer(customer);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return customerId;
    }

    public int getLastCustomerId(){
        int customerId = 0;
        try {
            customerId = customerDAO.getLastCustomerId();;
        } catch (SQLException e){
            e.printStackTrace();
        }

        return customerId;
    }
}
