package org.unibl.etf.model.purchases;

import org.unibl.etf.database.dao.implementations.CartItemDAOImpl;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.service.CartItemService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {
    private int id;
    private BigDecimal grandTotal;
    private int customerId;
    private List<CartItem> cartItems;

    private CartItemService cartItemService;

    public ShoppingCart(){
        cartItems = new ArrayList<>();

    }

    public ShoppingCart(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
        this.cartItemService = new CartItemService(new CartItemDAOImpl());
        this.cartItems = cartItemService.getAllCartItems(id);

        setupGrandTotal();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    private void setupGrandTotal(){
        this.grandTotal = BigDecimal.valueOf(0);
        for(CartItem cartItem : cartItems){
            this.grandTotal = grandTotal.add(cartItem.getSubTotal());
        }
    }

    private void addToGrandTotalFromItem(CartItem cartItem){
        BigDecimal newGrandTotal = grandTotal.add(cartItem.getSubTotal());
        grandTotal = newGrandTotal;
    }

    public void addToCart(CartItem cartItem){
        cartItems.add(cartItem);
        cartItemService.addCartItem(cartItem);

        addToGrandTotalFromItem(cartItem);
    }

    public void removeFromCart(CartItem cartItem){
        this.grandTotal = grandTotal.subtract(cartItem.getSubTotal());
        this.cartItems.remove(cartItem);
    }

    public boolean productExists(Product product){
        boolean exists = cartItems.stream().anyMatch(cartItem -> cartItem.getProduct().equals(product));

        return exists;
    }

    public void updateQuantityOfSameProduct(Product product, int quantity){
        Optional<CartItem> matchingCartItem = getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == product.getId())
                .findFirst();

        matchingCartItem.ifPresent(cartItem -> cartItem.setQuantity(quantity));
        matchingCartItem.ifPresent(this::addToGrandTotalFromItem);
        matchingCartItem.ifPresent(CartItem::updateSubTotal);
    }
}
