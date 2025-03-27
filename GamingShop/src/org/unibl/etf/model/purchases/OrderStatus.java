package org.unibl.etf.model.purchases;

import org.unibl.etf.model.enums.OrderStatusEnum;

public class OrderStatus {
    private int id;
    private OrderStatusEnum orderStatus;

    public OrderStatus(int id, OrderStatusEnum orderStatus) {
        this.id = id;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatus;
    }
    public void setOrderStatusEnum(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
        setId(orderStatus.ordinal() + 1);
    }
}
