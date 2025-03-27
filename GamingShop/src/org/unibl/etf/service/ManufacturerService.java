package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.ManufacturerDAO;
import org.unibl.etf.model.product.Manufacturer;

import java.sql.SQLException;
import java.util.List;

public class ManufacturerService {
    private ManufacturerDAO dao;

    public ManufacturerService(ManufacturerDAO dao) {
        this.dao = dao;
    }

    public List<Manufacturer> getAllManufacturers() throws SQLException {
        return dao.getAll();
    }
}
