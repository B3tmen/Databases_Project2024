package org.unibl.etf.service;

import org.unibl.etf.database.dao.interfaces.AdministratorDAO;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Customer;

import java.sql.SQLException;

public class AdministratorService {
    private AdministratorDAO administratorDAO;

    public AdministratorService(AdministratorDAO administratorDAO){
        this.administratorDAO = administratorDAO;
    }

    public int addAdministrator(Administrator administrator){
        int adminId = 0;

        try{
            adminId = administratorDAO.insert(administrator);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return adminId;
    }

    public int updateAdministrator(Administrator administrator){
        int updatedAdmins = 0;
        try {
            updatedAdmins = administratorDAO.update(administrator);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return updatedAdmins;
    }
}
