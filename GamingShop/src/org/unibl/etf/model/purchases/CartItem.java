package org.unibl.etf.model.purchases;

import org.unibl.etf.model.product.Product;

import java.math.BigDecimal;

public class CartItem {
    private int shoppingCartId;
    private Product product;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;


    public CartItem(int shoppingCartId, Product product, int quantity) {
        this.shoppingCartId = shoppingCartId;
        this.product = product;
        this.price = product.getDiscountedPrice() == null ? product.getPrice() : product.getDiscountedPrice();
        this.quantity = quantity;
        this.subTotal = price.multiply(BigDecimal.valueOf(quantity));
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }
    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
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
        updateSubTotal();
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public void updateSubTotal(){
        subTotal = price.multiply(BigDecimal.valueOf(quantity));
    }
}
