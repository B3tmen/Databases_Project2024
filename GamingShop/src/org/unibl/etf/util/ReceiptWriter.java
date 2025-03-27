package org.unibl.etf.util;

import org.unibl.etf.model.purchases.Order;
import org.unibl.etf.model.purchases.Receipt;
import org.unibl.etf.model.purchases.ReceiptItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class ReceiptWriter {
    private static final String RECEIPTS_PATH = "src/org/unibl/etf/resources/receipts/";

    private Receipt receipt;
    private Order order;
    private String recipientEmail;

    private BufferedWriter bw;

    public ReceiptWriter(Receipt receipt, Order order, String recipientEmail) {
        this.receipt = receipt;
        this.order = order;
        this.recipientEmail = recipientEmail;

        try {
            File file = new File(RECEIPTS_PATH + "Receipt_" + receipt.getId() + ".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Receipt getReceipt() {
        return receipt;
    }
    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void writeReceipt() {
        String receiptMessage = "";

        receiptMessage += "=".repeat(50);
        receiptMessage += String.format("%20s", "Thank You for Your purchase from Gaming Shop Commerce.");
        receiptMessage += "=".repeat(50);
        receiptMessage += String.format("\n%s %s\n", "Invoice ID:", receipt.getId());
        receiptMessage += "\n";

        receiptMessage += String.format("%-20s %50s\n", "Order ID:", "Bill To:");
        receiptMessage += String.format("%-20d %50s\n", order.getId(), recipientEmail);
        receiptMessage += "\n";

        receiptMessage += String.format("%-11s %50s\n", "Order Date:", "Source:");
        receiptMessage += String.format("%-10s %50s\n", order.getOrderDate().toString(), "Gaming Shop");
        receiptMessage += "\n";

        receiptMessage += "\nHERE'S WHAT YOU ORDERED:\n";
        receiptMessage += String.format("%-30s %-40s %-45s\n", "Description/Name", "Manufacturer/Publisher", "Price");

        for(ReceiptItem receiptItem : receipt.getReceiptItems()) {
            receiptMessage += String.format("%-30s %-40s %-45s\n", receiptItem.getProduct().getName(), receiptItem.getProduct().getManufacturerName(), receiptItem.getPrice());
        }
        receiptMessage += "\nTOTAL: " + receipt.getGrandTotal() + "\n";


        receiptMessage += "=".repeat(155);


        try {
            bw.write(receiptMessage);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
