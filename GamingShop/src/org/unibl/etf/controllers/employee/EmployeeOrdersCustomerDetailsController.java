package org.unibl.etf.controllers.employee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.unibl.etf.database.dao.implementations.AddressDAOImpl;
import org.unibl.etf.database.dao.implementations.CityDAOImpl;
import org.unibl.etf.database.dao.implementations.CountryDAOImpl;
import org.unibl.etf.database.dao.implementations.CustomerDAOImpl;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.service.AddressService;
import org.unibl.etf.service.CityService;
import org.unibl.etf.service.CountryService;
import org.unibl.etf.service.CustomerService;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeOrdersCustomerDetailsController implements Initializable {
    private Customer customer;

    private CustomerService customerService;
    private AddressService addressService;
    private CityService cityService;
    private CountryService countryService;

    @FXML
    private TextField countryTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField zipCodeTextField;

    public EmployeeOrdersCustomerDetailsController(int customerId) {
        this.customerService = new CustomerService(new CustomerDAOImpl());
        this.customer = customerService.getCustomerById(customerId);

        this.addressService = new AddressService(new AddressDAOImpl());
        this.cityService = new CityService(new CityDAOImpl());
        this.countryService = new CountryService(new CountryDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTextFields();
    }

    private void setupTextFields() {
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        phoneNumberTextField.setText(customer.getPhoneNumber());
        emailTextField.setText(customer.getEmail());

        int addressId = customer.getAdressId();
        Address address = addressService.getAddress(addressId);
        int cityId = address.getCityId();
        City city = cityService.getCity(cityId);
        int countryId = city.getCountryId();
        Country country = countryService.getCountry(countryId);

        countryTextField.setText(country.getName());
        addressTextField.setText(address.getAddress1());
        cityTextField.setText(city.getName());
        zipCodeTextField.setText(city.getZipCode());
    }
}
