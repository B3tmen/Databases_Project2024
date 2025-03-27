package org.unibl.etf.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import org.unibl.etf.database.dao.implementations.*;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.*;
import org.unibl.etf.util.AlertShower;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminUpdateAccountController implements Initializable {
    private User user;

    private CountryService countryService;
    private CityService cityService;
    private AddressService addressService;
    private CustomerService customerService;
    private EmployeeService employeeService;
    private AdministratorService administratorService;

    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TitledPane employeeSpecificTitledPane;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField accountTypeTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TitledPane customerSpecificTitledPane;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private ComboBox activeComboBox;

    public AdminUpdateAccountController(User user) {
        this.user = user;
        this.countryService = new CountryService(new CountryDAOImpl());
        this.cityService = new CityService(new CityDAOImpl());
        this.addressService = new AddressService(new AddressDAOImpl());
        this.customerService = new CustomerService(new CustomerDAOImpl());
        this.employeeService = new EmployeeService(new EmployeeDAOImpl());
        this.administratorService = new AdministratorService(new AdministratorDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTextFields();
        setupComboBox();
    }

    private void setupComboBox(){
        activeComboBox.getItems().addAll( "TRUE", "FALSE");
    }

    private void setupTextFields(){
        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPasswordHash());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        emailTextField.setText(user.getEmail());
        accountTypeTextField.setText(user.getUserType());

        if(user instanceof Administrator){
            customerSpecificTitledPane.setDisable(true);
            employeeSpecificTitledPane.setDisable(true);
        }
        else if(user instanceof Employee employee){
            customerSpecificTitledPane.setDisable(true);
            positionTextField.setText(employee.getPosition());
            salaryTextField.setText(employee.getSalary().toString());
        }
        else if(user instanceof Customer customer){
            employeeSpecificTitledPane.setDisable(true);
            phoneNumberTextField.setText(customer.getPhoneNumber());

            Address address = addressService.getAddress(customer.getAdressId());
            City city = cityService.getCity(address.getCityId());
            Country country = countryService.getCountry(city.getCountryId());

            countryTextField.setText(country.getName());
            cityTextField.setText(city.getName());
            zipCodeTextField.setText(city.getZipCode());
            addressTextField.setText(address.getAddress1());
        }
    }


    @FXML
    public void finishUpdatingAccountAction(ActionEvent actionEvent) {
        boolean isValid = checkFields();
        int updatedUsers = 0;
        if(isValid){
            User user1 = null;

            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String type = accountTypeTextField.getText();
            String isActiveString = activeComboBox.getSelectionModel().getSelectedItem().toString();
            boolean isActive = false;
            switch (isActiveString){
                case "TRUE" -> isActive = true;
                case "FALSE" -> isActive = false;
            }

            String position = positionTextField.getText();
            String salary = salaryTextField.getText();

            String phoneNumber = phoneNumberTextField.getText();
            String countryName = countryTextField.getText();
            String cityName = cityTextField.getText();
            String zipCode = zipCodeTextField.getText();
            String addressName = addressTextField.getText();

            if("Administrator".equals(type)){
                user1 = new Administrator(user.getId(), username, password, firstName, lastName, email, isActive);
                updatedUsers = administratorService.updateAdministrator((Administrator) user1);
            }
            else if("Employee".equals(type)){
                user1 = new Employee(user.getId(), username, password, firstName, lastName, email, isActive, position, new BigDecimal(salary));
                updatedUsers = employeeService.updateEmployee((Employee) user1);
            }
            else if("Customer".equals(type)){
                // Check for existing country
                int countryId = countryService.getCountryIdByName(countryName);
                if (countryId == 0) {
                    Country country = new Country(0, countryName);
                    countryId = countryService.addCountry(country);
                }

                // Check for existing city
                int cityId = cityService.getCityIdByNameAndCountry(cityName, countryId);
                if (cityId == 0) {
                    City city = new City(0, cityName, zipCode, countryId);
                    cityId = cityService.addCity(city);
                }

                // Check for existing address
                int addressId = addressService.getAddressIdByNameAndCity(addressName, cityId);
                if (addressId == 0) {
                    Address address = new Address(0, addressName, "", cityId);  //TODO: change address2 to real address
                    addressId = addressService.addAddress(address);
                }

                user1 = new Customer(user.getId(), username, password, firstName, lastName, email, isActive, phoneNumber, addressId);
                updatedUsers = customerService.updateCustomer((Customer) user1);
            }

            if(updatedUsers > 0){
                AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Successfully updated a User!", "");
                alertShower.showAlert();

                Node node = (Node) actionEvent.getSource();     // closing the current stage/login window
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
            else{
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Something went wrong.", "Try again.");
                alertShower.showAlert();
            }
        }
    }

    private boolean checkFields() {
        boolean isValid = true;

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String type = accountTypeTextField.getText();
        String isActive = (activeComboBox.getSelectionModel().getSelectedItem() == null ? "" : activeComboBox.getSelectionModel().getSelectedItem().toString());

        String position = positionTextField.getText();
        String salary = salaryTextField.getText();

        String phoneNumber = phoneNumberTextField.getText();
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();
        String address = addressTextField.getText();

        String missingFields = "";
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || isActive.isEmpty()) {
            if (username.isEmpty()) missingFields += "Username, ";
            if (password.isEmpty()) missingFields += "Password, ";
            if (firstName.isEmpty()) missingFields += "First Name, ";
            if (lastName.isEmpty()) missingFields += "Last Name, ";
            if (email.isEmpty()) missingFields += "Email, ";
            if (isActive.isEmpty()) missingFields += "Active, ";

            isValid = false;
            // Remove the last comma and space
            if (!missingFields.isEmpty()) {
                missingFields = missingFields.substring(0, missingFields.length() - 2);

                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                alertShower.showAlert();
            }

        }

        if("Employee".equals(type)){

            if (position.isEmpty() || salary.isEmpty()){
                isValid = false;

                if (position.isEmpty()) missingFields += "Position, ";
                if (salary.isEmpty()) missingFields += "Salary, ";
                // Remove the last comma and space
                if (!missingFields.isEmpty()) {
                    missingFields = missingFields.substring(0, missingFields.length() - 2);

                    AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                    alertShower.showAlert();
                }

            }
        }
        else if("Customer".equals(type)){
            if (phoneNumber.isEmpty() || country.isEmpty() || address.isEmpty() || city.isEmpty() || zipCode.isEmpty()){
                isValid = false;

                if (phoneNumber.isEmpty()) missingFields += "Phone Number, ";
                if (country.isEmpty()) missingFields += "Country, ";
                if (address.isEmpty()) missingFields += "Address, ";
                if (city.isEmpty()) missingFields += "City, ";
                if (zipCode.isEmpty()) missingFields += "Zip Code, ";
                // Remove the last comma and space
                if (!missingFields.isEmpty()) {
                    missingFields = missingFields.substring(0, missingFields.length() - 2);

                    AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                    alertShower.showAlert();
                }

            }
        }


        return isValid;
    }
}
