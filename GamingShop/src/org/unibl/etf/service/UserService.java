package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.UserDAO;
import org.unibl.etf.model.users.User;
import org.unibl.etf.util.TextHasher;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getUsers() {
        List<User> users = null;
        try{
            users = userDAO.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    public int setUserActivationStatus(int userId, boolean activationStatus) {
        int deactivatedUsers = 0;

        try{
            deactivatedUsers = userDAO.setUserActivationStatus(userId, activationStatus);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return deactivatedUsers;
    }

    public User getLoginUser(String username, String password) throws SQLException {
        User user = userDAO.getByUsernameAndPassword(username, TextHasher.getHash(password));
        return user;
    }
}
