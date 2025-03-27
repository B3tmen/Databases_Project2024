package org.unibl.etf.service;

import org.unibl.etf.database.dao.implementations.CountryDAOImpl;
import org.unibl.etf.database.dao.interfaces.CountryDAO;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.Country;

import java.sql.SQLException;

public class CountryService {
    private CountryDAO countryDAO;

    public CountryService(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    public Country getCountry(int id) {
        Country country = null;
        try{
            country = countryDAO.get(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return country;
    }

    public int addCountry(Country country) {
        int id = -1;
        try {
            id = countryDAO.insert(country);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int getCountryIdByName(String name){
        int id = 0;
        try{
            id = countryDAO.getCountryIdByName(name);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }
}
