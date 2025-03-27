package org.unibl.etf.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.unibl.etf.database.dao.interfaces.CountryDAO;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.enums.UserTypeEnum;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.*;
import org.unibl.etf.util.AlertShower;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminAddAccountController implements Initializable {
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
    private TextField salaryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private ComboBox accountTypeComboBox;
    @FXML
    private TitledPane employeeSpecificTitledPane;
    @FXML
    private TitledPane customerSpecificTitledPane;

    public AdminAddAccountController(){
        countryService = new CountryService(new CountryDAOImpl());
        cityService = new CityService(new CityDAOImpl());
        addressService = new AddressService(new AddressDAOImpl());
        customerService = new CustomerService(new CustomerDAOImpl());
        employeeService = new EmployeeService(new EmployeeDAOImpl());
        administratorService = new AdministratorService(new AdministratorDAOImpl());
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTitledPanes();
        setupComboBox();
    }

    private void setupTitledPanes() {
        employeeSpecificTitledPane.setDisable(true);
        customerSpecificTitledPane.setDisable(true);
    }

    private void setupComboBox(){
        accountTypeComboBox.getItems().addAll(UserTypeEnum.ADMINISTRATOR.getType(), UserTypeEnum.EMPLOYEE.getType(), UserTypeEnum.CUSTOMER.getType());

        accountTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if("Administrator".equals(newValue)){
                    employeeSpecificTitledPane.setDisable(true);
                    customerSpecificTitledPane.setDisable(true);
                }
                else if("Employee".equals(newValue)){
                    employeeSpecificTitledPane.setDisable(false);
                    customerSpecificTitledPane.setDisable(true);
                }
                else if("Customer".equals(newValue)){
                    employeeSpecificTitledPane.setDisable(true);
                    customerSpecificTitledPane.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void finishAddingAccountAction(ActionEvent actionEvent) {
        boolean isValid = checkFields();

        if(isValid){
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String type = accountTypeComboBox.getSelectionModel().getSelectedItem().toString();

            String position = positionTextField.getText();
            String salary = salaryTextField.getText();

            String phoneNumber = phoneNumberTextField.getText();
            String countryName = countryTextField.getText();
            String cityName = cityTextField.getText();
            String zipCode = zipCodeTextField.getText();
            String addressName = addressTextField.getText();


            User user = null;
            int userId = 0;
            if("Administrator".equals(type)){
                user = new Administrator(0, username, password, firstName, lastName, email, true);
                userId = administratorService.addAdministrator((Administrator) user);
            }
            else if("Employee".equals(type)){
                BigDecimal salaryBigDecimal = new BigDecimal(salary);

                user = new Employee(0, username, password, firstName, lastName, email, true, position, salaryBigDecimal);
                userId = employeeService.addEmployee((Employee) user);
            }
            else if("Customer".equals(type)){
                Country country = new Country(0, countryName);
                int countryId = countryService.addCountry(country);
                City city = new City(0, cityName, zipCode, countryId);
                int cityId = cityService.addCity(city);
                Address address = new Address(0, addressName, zipCode, cityId);
                int addressId = addressService.addAddress(address);

                user = new Customer(0, username, password, firstName, lastName, email, true, phoneNumber, addressId);
                userId = customerService.addCustomer((Customer) user);
            }
            System.out.println("USER ID FROM ADD: " + userId);

            if(userId > 0){
                AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "You successfully added an Account", "");
                alertShower.showAlert();

                Node node = (Node) actionEvent.getSource();     // closing the current stage/window
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
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
        String type = accountTypeComboBox.getSelectionModel().getSelectedItem() != null ? accountTypeComboBox.getSelectionModel().getSelectedItem().toString() : "";

        String position = positionTextField.getText();
        String salary = salaryTextField.getText();

        String phoneNumber = phoneNumberTextField.getText();
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String zipCode = zipCodeTextField.getText();
        String address = addressTextField.getText();

        String missingFields = "";
        if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            if (username.isBlank()) missingFields += "Username, ";
            if (password.isBlank()) missingFields += "Password, ";
            if (firstName.isBlank()) missingFields += "First Name, ";
            if (lastName.isBlank()) missingFields += "Last Name, ";
            if (email.isBlank()) missingFields += "Email, ";

            isValid = false;
            // Remove the last comma and space
            if (!missingFields.isBlank()) {
                missingFields = missingFields.substring(0, missingFields.length() - 2);
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                alertShower.showAlert();
            }

        }
        if(type.isBlank()){
            missingFields += "Account type, ";

            isValid = false;
            // Remove the last comma and space
            if (!missingFields.isBlank()) {
                missingFields = missingFields.substring(0, missingFields.length() - 2);
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                alertShower.showAlert();
            }

        }
        else{
            if("Employee".equals(type)){
                if (position.isBlank() || salary.isBlank()){
                    isValid = false;
                }

                if (position.isBlank()) missingFields += "Position, ";
                if (salary.isBlank()) missingFields += "Salary, ";
                // Remove the last comma and space
                if (!missingFields.isBlank()) {
                    missingFields = missingFields.substring(0, missingFields.length() - 2);
                    AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                    alertShower.showAlert();
                }

            }
            else if("Customer".equals(type)){
                if (phoneNumber.isBlank() || country.isBlank() || address.isBlank() || city.isBlank() || zipCode.isBlank()){
                    isValid = false;
                }

                if (phoneNumber.isBlank()) missingFields += "Phone Number, ";
                if (country.isBlank()) missingFields += "Country, ";
                if (address.isBlank()) missingFields += "Address, ";
                if (city.isBlank()) missingFields += "City, ";
                if (zipCode.isBlank()) missingFields += "Zip Code, ";
                // Remove the last comma and space
                if (!missingFields.isBlank()) {
                    missingFields = missingFields.substring(0, missingFields.length() - 2);
                    AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", missingFields + " field(s) weren't inputted! (empty)", "Try again.");
                    alertShower.showAlert();
                }

            }
        }
        
        return isValid;
    }
}
