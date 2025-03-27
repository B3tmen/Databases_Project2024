package org.unibl.etf.model.users;

import org.unibl.etf.model.enums.UserTypeEnum;

public class Customer extends User {
    private String phoneNumber;
    private int adressId;

    public Customer() {}

    public Customer(int id, String username, String password, String firstName, String lastName,
                    String email, boolean isActive, String phoneNumber, int addressId){

        super(id, username, password, firstName, lastName, email, UserTypeEnum.CUSTOMER.getType(), isActive);

        this.phoneNumber = phoneNumber;
        this.adressId = addressId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAdressId() {
        return adressId;
    }
    public void setAdressId(int adressId) {
        this.adressId = adressId;
    }
}
