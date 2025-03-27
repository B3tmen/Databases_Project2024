package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ReceiptDAO;
import org.unibl.etf.model.purchases.Receipt;

import java.sql.SQLException;

public class ReceiptService {
    private ReceiptDAO receiptDAO;

    public ReceiptService(ReceiptDAO receiptDAO) {
        this.receiptDAO = receiptDAO;
    }

    public int addReceipt(Receipt receipt) {
        int receiptId = 0;
        try{
            receiptId = receiptDAO.insert(receipt);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return receiptId;
    }
}
