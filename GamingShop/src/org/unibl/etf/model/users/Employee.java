package org.unibl.etf.model.users;

import org.unibl.etf.model.enums.UserTypeEnum;

import java.math.BigDecimal;

public class Employee extends User {
    private String position;
    private BigDecimal salary;

    public Employee(){}

    public Employee(int id, String username, String passwordHash, String firstName,
                    String lastName, String email, boolean isActive, String position, BigDecimal salary) {

        super(id, username, passwordHash, firstName, lastName, email, UserTypeEnum.EMPLOYEE.getType(), isActive);
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
