package org.unibl.etf.model.purchases;

import org.unibl.etf.model.product.Product;

import java.math.BigDecimal;

public class ReceiptItem {
    private int receiptId;
    private Product product;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;

    public ReceiptItem(int receiptId, Product product, BigDecimal price, int quantity, BigDecimal subTotal) {
        this.receiptId = receiptId;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public int getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
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
}
