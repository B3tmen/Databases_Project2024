package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.AddressDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.model.address.Address;

import java.sql.*;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address get(int id) throws SQLException {
        Address address = null;
        String sql = "SELECT * FROM address WHERE address_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                address = new Address(
                        rs.getInt("address_id"),
                        rs.getString("address_1"),
                        rs.getString("address_2"),
                        rs.getInt("fk_city_id")
                );
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return address;
    }

    @Override
    public List<Address> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int insert(Address object) throws SQLException {
        int id = -1;
        String sql = "CALL usp_AddAddress(?, ?, ?)";

        Connection conn = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, object.getAddress1());
            cstmt.setString(2, object.getAddress2());
            cstmt.setInt(3, object.getCityId());

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
    public int update(Address object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Address object) throws SQLException {
        return 0;
    }

    @Override
    public int getAddressIdByNameAndCity(String address1Name, int cityId) throws SQLException {
        int addressId = 0;
        String sql = "SELECT address_id FROM address WHERE address_1 = ? AND fk_city_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, address1Name);
            pstmt.setInt(2, cityId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                addressId = rs.getInt("address_id");
            }
        }
        finally {
            ConnectionPool.getInstance().checkIn(conn);
            pstmt.close();
        }

        return addressId;
    }
}
