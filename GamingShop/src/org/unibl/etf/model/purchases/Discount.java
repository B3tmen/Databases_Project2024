package org.unibl.etf.model.purchases;

import java.math.BigDecimal;
import java.sql.Date;

public class Discount {
    private int id;
    private BigDecimal percentage;
    private Date dateFrom;
    private Date dateTo;
    private int employeeId;


    public Discount(int id, BigDecimal percentage, Date dateFrom, Date dateTo, int employeeId) {
        this.id = id;
        this.percentage = percentage;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.employeeId = employeeId;
    }

    public Discount(BigDecimal percentage, Date dateFrom, Date dateTo) {
        this.id = 0;
        this.percentage = percentage;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.employeeId = 0;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Date getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
