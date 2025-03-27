package org.unibl.etf.model.users;

import org.unibl.etf.model.enums.UserTypeEnum;

public class Administrator extends User {

    public Administrator(){}

    public Administrator(int id, String username, String passwordHash, String firstName, String lastName,
                         String email, boolean isActive) {
        super(id, username, passwordHash, firstName, lastName, email, UserTypeEnum.ADMINISTRATOR.getType(), isActive);
    }
}
