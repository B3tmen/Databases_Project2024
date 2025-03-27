package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.CityDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.database.mysql.DatabaseConnection;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;

import java.sql.*;
import java.util.List;

public class CityDAOImpl implements CityDAO {

    @Override
    public City get(int id) throws SQLException {
        City city = null;
        String sql = "SELECT * FROM city WHERE city_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                city = new City(
                        rs.getInt("city_id"),
                        rs.getString("name"),
                        rs.getString("zip_code"),
                        rs.getInt("fk_country_id")
                );
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return city;
    }

    @Override
    public List<City> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(City object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddCity(?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, object.getName());
            cstmt.setString(2, object.getZipCode());
            cstmt.setInt(3, object.getCountryId());

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            cstmt.close();
        }

        return id;
    }

    @Override
    public int update(City object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(City object) {
        return 0;
    }

    @Override
    public int getCityIdByNameAndCountry(String cityName, int countryId) throws SQLException {
        int cityId = 0;
        String sql = "SELECT city_id FROM city WHERE name = ? AND fk_country_id = ?";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, cityName);
            pstmt.setInt(2, countryId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                cityId = rs.getInt("city_id");
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return cityId;
    }
}
