package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.AddressDAO;
import org.unibl.etf.model.address.Address;

import java.sql.SQLException;

public class AddressService {
    private AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Address getAddress(int id) {
        Address address = null;
        try{
            address = addressDAO.get(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return address;
    }

    public int addAddress(Address address) {
        int id = -1;
        try {
            id = addressDAO.insert(address);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int getAddressIdByNameAndCity(String name, int cityId){
        int id = 0;
        try{
            id = addressDAO.getAddressIdByNameAndCity(name, cityId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }
}
