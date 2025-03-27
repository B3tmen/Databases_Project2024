package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.users.Employee;

public interface EmployeeDAO extends DAO<Employee> {
    Employee getEmployeeById(int id);
}
