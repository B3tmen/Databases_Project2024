package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.product.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO extends DAO<Product>{
    List<Product> getPcComponentsProducts() throws SQLException;
    List<Product> getCpuProducts() throws SQLException;
    List<Product> getGpuProducts() throws SQLException;
    List<Product> getRamProducts() throws SQLException;
    List<Product> getStorageProducts() throws SQLException;
    List<Product> getMotherboardProducts() throws SQLException;

    List<Product> getPcPeripheralsProducts() throws SQLException;
    List<Product> getKeyboardProducts() throws SQLException;
    List<Product> getMiceProducts() throws SQLException;
    List<Product> getHeadphonesProducts() throws SQLException;

    List<Product> getConsolesProducts() throws SQLException;
    List<Product> getPlaystationProducts() throws SQLException;
    List<Product> getXBoxProducts() throws SQLException;

    List<Product> getGamesProducts() throws SQLException;
    List<Product> getCablesAndAdaptersProducts() throws SQLException;
}
