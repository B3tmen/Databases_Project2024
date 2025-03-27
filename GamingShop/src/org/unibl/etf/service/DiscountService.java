package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.DiscountDAO;
import org.unibl.etf.model.purchases.Discount;

import java.sql.SQLException;

public class DiscountService {
    private DiscountDAO discountDAO;

    public DiscountService(DiscountDAO discountDAO) {
        this.discountDAO = discountDAO;
    }

    public int addDiscount(Discount discount) {
        int discountId = 0;

        try{
            discountId = discountDAO.insert(discount);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return discountId;
    }

    public Discount getDiscountById(int id) {
        Discount discount = null;
        try {
            discount = discountDAO.get(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discount;
    }

    public int updateDiscount(Discount discount) {
        int updatedDiscounts = 0;
        try {
            updatedDiscounts = discountDAO.update(discount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedDiscounts;
    }
}
