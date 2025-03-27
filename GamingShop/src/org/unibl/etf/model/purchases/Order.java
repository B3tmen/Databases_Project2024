package org.unibl.etf.model.purchases;

import org.unibl.etf.model.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private BigDecimal shipping;
    private BigDecimal grandTotal;
    private Date orderDate;             // java.sql.Date instead of java.util.Date for type safety
    private OrderStatus orderStatus;
    private int customerId;

    private List<OrderItem> orderItems;

    public Order(int id, BigDecimal shipping, BigDecimal grandTotal, Date orderDate, OrderStatus orderStatus, int customerId) {
        this.id = id;
        this.shipping = shipping;
        this.grandTotal = grandTotal;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.orderItems = new ArrayList<OrderItem>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getShipping() {
        return shipping;
    }
    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }
}
