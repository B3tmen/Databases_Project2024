package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.CityDAO;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;

import java.sql.SQLException;

public class CityService {
    private CityDAO cityDAO;

    public CityService(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    public City getCity(int id) {
        City city = null;
        try{
            city = cityDAO.get(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return city;
    }

    public int addCity(City city) {
        int id = -1;

        try {
            id = cityDAO.insert(city);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getCityIdByNameAndCountry(String name, int countryId){
        int id = 0;
        try{
            id = cityDAO.getCityIdByNameAndCountry(name, countryId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }
}
