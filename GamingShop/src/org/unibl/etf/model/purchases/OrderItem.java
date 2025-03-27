package org.unibl.etf.model.purchases;

import org.unibl.etf.model.product.Product;

import java.math.BigDecimal;

public class OrderItem {
    private int orderId;
    private Product product;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;


    public OrderItem(int orderId, Product product, int quantity) {
        this.orderId = orderId;
        this.product = product;
        this.price = product.getDiscountedPrice() == null ? product.getPrice() : product.getDiscountedPrice();
        this.quantity = quantity;
        this.subTotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
