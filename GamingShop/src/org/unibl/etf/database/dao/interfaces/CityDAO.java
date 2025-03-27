package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.address.City;

import java.sql.SQLException;

public interface CityDAO extends DAO<City>{
    int getCityIdByNameAndCountry(String cityName, int countryId) throws SQLException;
}
