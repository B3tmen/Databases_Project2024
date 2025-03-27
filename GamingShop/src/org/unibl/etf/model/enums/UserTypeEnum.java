package org.unibl.etf.model.enums;

public enum UserTypeEnum {
    CUSTOMER("Customer"), EMPLOYEE("Employee"), ADMINISTRATOR("Administrator");
    private final String type;

    UserTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
