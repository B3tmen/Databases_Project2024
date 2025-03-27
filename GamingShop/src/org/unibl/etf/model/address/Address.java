package org.unibl.etf.model.address;

public class Address {
    private int addressId;
    private String address1;
    private String address2;
    private int cityId;

    public Address(int addressId, String address1, String address2, int cityId) {
        this.addressId = addressId;
        this.address1 = address1;
        this.address2 = address2;
        this.cityId = cityId;
    }

    public int getAddressId() {
        return addressId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
