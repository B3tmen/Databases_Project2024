package org.unibl.etf.database.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

/** Data Access Object pattern generic interface*/
public interface DAO<T> {
    /**Retrieves a record in the database table by ID.
     * @return record type that was retrieved*/
    T get(int id) throws SQLException;

    /**Retrieves all records in the database table.
     * @return List of record type that were retrieved*/
    List<T> getAll() throws SQLException;

    /**Creates a record in the database table.
     * @return number of inserted objects*/
    int insert(T object) throws SQLException;

    /**Updates a record in the database table.
     * @return number of inserted objects*/
    int update(T object) throws SQLException;

    /**Deletes a record in the database table.
     * @return Number of deleted records*/
    int delete(T object) throws SQLException;
}
