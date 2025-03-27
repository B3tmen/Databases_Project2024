package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ReceiptItemDAO;
import org.unibl.etf.model.purchases.ReceiptItem;

import java.sql.SQLException;

public class ReceiptItemService {
    private ReceiptItemDAO receiptItemDAO;

    public ReceiptItemService(ReceiptItemDAO receiptItemDAO) {
        this.receiptItemDAO = receiptItemDAO;
    }

    public int addReceiptItem(ReceiptItem receiptItem) {
        int addedItems = 0;

        try{
            addedItems = receiptItemDAO.insert(receiptItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addedItems;
    }

}
