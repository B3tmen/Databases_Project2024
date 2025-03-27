package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.EmployeeDAO;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;

import java.sql.SQLException;

public class EmployeeService {
    private EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public int addEmployee(Employee employee) {
        int employeeId = 0;
        try{
            employeeId = employeeDAO.insert(employee);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return employeeId;
    }

    public int updateEmployee(Employee employee){
        int updatedEmployees = 0;
        try {
            updatedEmployees = employeeDAO.update(employee);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return updatedEmployees;
    }



    public Employee getEmployeeById(int id) {
        Employee employee = null;

        try{
            employee = employeeDAO.get(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return employee;
    }
}
