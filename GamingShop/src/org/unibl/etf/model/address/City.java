package org.unibl.etf.model.address;

public class City {
    private int id;
    private String name;
    private String zipCode;
    private int countryId;

    public City(int id, String name, String zipCode, int countryId){
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.countryId = countryId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getCountryId() {
        return countryId;
    }
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
