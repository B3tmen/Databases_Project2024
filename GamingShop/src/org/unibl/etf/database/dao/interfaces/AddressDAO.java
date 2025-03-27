package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.address.Address;

import java.sql.SQLException;

public interface AddressDAO extends DAO<Address> {
    int getAddressIdByNameAndCity(String address1Name, int cityId) throws SQLException;
}
