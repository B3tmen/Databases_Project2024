package org.unibl.etf.database.dao.interfaces;

import org.unibl.etf.model.users.User;

import java.sql.SQLException;

public interface UserDAO extends DAO<User> {
    User getByUsernameAndPassword(String username, String passwordHash) throws SQLException;
    int setUserActivationStatus(int userId, boolean activationStatus) throws SQLException;
}
