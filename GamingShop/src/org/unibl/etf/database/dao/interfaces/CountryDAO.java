package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.address.Country;

import java.sql.SQLException;

public interface CountryDAO extends DAO<Country>{
    int getCountryIdByName(String countryName) throws SQLException;
}
