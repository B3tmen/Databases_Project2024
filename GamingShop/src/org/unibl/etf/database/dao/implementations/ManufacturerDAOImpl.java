package org.unibl.etf.database.dao.implementations;

import org.unibl.etf.database.dao.interfaces.ManufacturerDAO;
import org.unibl.etf.database.mysql.ConnectionPool;
import org.unibl.etf.database.mysql.DatabaseConnection;
import org.unibl.etf.model.product.Manufacturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerDAOImpl implements ManufacturerDAO {

    @Override
    public Manufacturer get(int id) throws SQLException {
        Manufacturer manufacturer = null;
        String sql = "SELECT * FROM manufacturer WHERE manufacturer_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                manufacturer = new Manufacturer(
                        rs.getInt("manufacturer_id"),
                        rs.getString("name")
                );
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
        }

        return manufacturer;
    }

    @Override
    public List<Manufacturer> getAll() throws SQLException {
        List<Manufacturer> manufacturers = new ArrayList<>();
        String sql = "SELECT * FROM manufacturer";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionPool.getInstance().checkOut();
            pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Manufacturer manufacturer = new Manufacturer(
                        rs.getInt("manufacturer_id"),
                        rs.getString("name")
                );

                manufacturers.add(manufacturer);
            }

        }
        finally{
            ConnectionPool.getInstance().checkIn(conn);
        }

        return manufacturers;
    }

    @Override
    public int insert(Manufacturer object) throws SQLException {
        return 0;
    }

    @Override
    public int update(Manufacturer object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Manufacturer object) {
        return 0;
    }
}
