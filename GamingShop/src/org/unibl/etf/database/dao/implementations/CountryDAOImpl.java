package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.CountryDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.address.Country;

import java.sql.*;
import java.util.List;

public class CountryDAOImpl implements CountryDAO {

    @Override
    public Country get(int id) throws SQLException {
        Country country = null;
        String sql = "SELECT * FROM country WHERE country_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                country = new Country(
                        rs.getInt("country_id"),
                        rs.getString("name")
                );
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return country;
    }

    @Override
    public List<Country> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Country object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddCountry(?)";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, object.getName());

            ResultSet rs = cstmt.executeQuery();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return id;
    }

    @Override
    public int update(Country object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Country object) {
        return 0;
    }

    @Override
    public int getCountryIdByName(String countryName) throws SQLException {
        int countryId = 0;
        String sql = "SELECT country_id FROM country WHERE name = ?";

        Connection conn = ConnectionPool.getInstance().checkOut();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, countryName);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                countryId = rs.getInt("country_id");
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
        }

        return countryId;
    }
}
