package org.unibl.etf.model.enums;

public enum OrderStatusEnum {
    //'pending', 'completed', 'cancelled'
    PENDING("pending"), COMPLETED("completed"), SHIPPED("shipped"), CANCELLED("cancelled");

    private String status;
    OrderStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
