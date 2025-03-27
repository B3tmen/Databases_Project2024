package org.unibl.etf.model.purchases;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private int id;
    private Timestamp dateIssued;
    private List<ReceiptItem> receiptItems;
    private BigDecimal grandTotal;
    private Order order;


    public Receipt(int id, Order order, Timestamp dateIssued) {
        this.id = id;
        this.dateIssued = dateIssued;
        this.receiptItems = new ArrayList<ReceiptItem>();
        this.grandTotal = BigDecimal.ZERO;
        this.order = order;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateIssued() {
        return dateIssued;
    }
    public void setDateIssued(Timestamp dateIssued) {
        this.dateIssued = dateIssued;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }
    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public void addReceiptItem(ReceiptItem receiptItem) {
        receiptItems.add(receiptItem);

        updateGrandTotal(receiptItem.getSubTotal());
    }

    private void updateGrandTotal(BigDecimal subTotal){
        this.grandTotal = this.grandTotal.add(subTotal);
    }
}
