package org.unibl.etf.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.unibl.etf.database.dao.implementations.AddressDAOImpl;
import org.unibl.etf.database.dao.implementations.CityDAOImpl;
import org.unibl.etf.database.dao.implementations.CountryDAOImpl;
import org.unibl.etf.database.dao.implementations.CustomerDAOImpl;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.enums.UserTypeEnum;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.service.AddressService;
import org.unibl.etf.service.CityService;
import org.unibl.etf.service.CountryService;
import org.unibl.etf.service.CustomerService;
import org.unibl.etf.util.AlertShower;

import java.net.URL;
import java.util.ResourceBundle;

public class UserRegisterController implements Initializable {
    private CustomerService customerService;
    private CityService cityService;
    private CountryService countryService;
    private AddressService addressService;


    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private PasswordField passwordRegularPasswordField;
    @FXML
    private PasswordField passwordConfirmPasswordField;
    @FXML
    private TextField passwordConfirmRevealedTextField;
    @FXML
    private TextField passwordRegularRevealedTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField zipcodeTextField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button showPasswordRegularButton;
    @FXML
    private Button unshowPasswordRegularButton;
    @FXML
    private Button showPasswordConfirmButton;
    @FXML
    private Button unshowPasswordConfirmButton;
    @FXML
    private TextField address1TextField;
    @FXML
    private TextField address2TextField;


    public UserRegisterController() {
        customerService = new CustomerService(new CustomerDAOImpl());
        cityService = new CityService(new CityDAOImpl());
        countryService = new CountryService(new CountryDAOImpl());
        addressService = new AddressService(new AddressDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPasswordTextFields();
        setupSeePasswordButtons();
    }


    @FXML
    public void signUpUser(ActionEvent actionEvent) {
        boolean isValid = checkFields();
        if (isValid) {
            String username = usernameTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String phoneNumber = phoneNumberTextField.getText();
            String password = passwordRegularPasswordField.getText();
            String confirmPassword = passwordConfirmPasswordField.getText();
            String countryName = countryTextField.getText();
            String cityName = cityTextField.getText();
            String zipcode = zipcodeTextField.getText();
            String address1 = address1TextField.getText();
            String address2 = address2TextField.getText();


            if(password.equals(confirmPassword)) {
                // Check for existing country
                int countryId = countryService.getCountryIdByName(countryName);
                if (countryId == 0) {
                    Country country = new Country(0, countryName);
                    countryId = countryService.addCountry(country);
                }

                // Check for existing city
                int cityId = cityService.getCityIdByNameAndCountry(cityName, countryId);
                if (cityId == 0) {
                    City city = new City(0, cityName, zipcode, countryId);
                    cityId = cityService.addCity(city);
                }

                // Check for existing address
                int addressId = addressService.getAddressIdByNameAndCity(address1, cityId);
                if (addressId == 0) {
                    Address address = new Address(0, address1, address2, cityId);
                    addressId = addressService.addAddress(address);
                }

                // Dodaj korisnika
                Customer customer = new Customer(0, username, password, firstName, lastName, email, true, phoneNumber, addressId);
                customerService.addCustomer(customer);

                AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "You have successfully registered an Account", "Try logging in now!");
                alertShower.showAlert();

                Node node = (Node) actionEvent.getSource();     // closing the current stage/login window
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
            else{
                showErrorDialog("Passwords do not match");
            }
        }
        else{
            showErrorDialog("All fields must be filled");
        }
    }

    private boolean checkFields(){
        boolean isValid = true;
        String username = usernameTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String password = passwordRegularPasswordField.getText();
        String confirmPassword = passwordConfirmPasswordField.getText();
        String country = countryTextField.getText();
        String cityName = cityTextField.getText();
        String zipcode = zipcodeTextField.getText();
        String address1 = address1TextField.getText();

        if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || country.isEmpty() || cityName.isEmpty() || zipcode.isEmpty() || address1.isEmpty()){
            isValid = false;
        }

        return isValid;
    }

    private boolean checkCountryExists(){
        boolean exists = false;

        return exists;
    }
    private boolean checkCityExists(){
        boolean exists = false;

        return exists;
    }
    private boolean checkAddressExists(){
        boolean exists = false;

        return exists;
    }

    @FXML
    public void showRegularPassword(ActionEvent actionEvent) {
        showPasswordRegularButton.setVisible(false);
        unshowPasswordRegularButton.setVisible(true);

        passwordRegularPasswordField.setVisible(false);
        passwordRegularRevealedTextField.setVisible(true);
    }

    @FXML
    public void unshowRegularPassword(ActionEvent actionEvent) {
        showPasswordRegularButton.setVisible(true);
        unshowPasswordRegularButton.setVisible(false);

        passwordRegularPasswordField.setVisible(true);
        passwordRegularRevealedTextField.setVisible(false);
    }

    @FXML
    public void showConfirmPassword(ActionEvent actionEvent) {
        showPasswordConfirmButton.setVisible(false);
        unshowPasswordConfirmButton.setVisible(true);

        passwordConfirmPasswordField.setVisible(false);
        passwordConfirmRevealedTextField.setVisible(true);
    }

    @FXML
    public void unshowConfirmPassword(ActionEvent actionEvent) {
        showPasswordConfirmButton.setVisible(true);
        unshowPasswordConfirmButton.setVisible(false);

        passwordConfirmPasswordField.setVisible(true);
        passwordConfirmRevealedTextField.setVisible(false);
    }



    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void setupSeePasswordButtons(){
        double x1 = showPasswordRegularButton.getLayoutX(), y1 = showPasswordRegularButton.getLayoutY();
        unshowPasswordRegularButton.setLayoutX(x1);
        unshowPasswordRegularButton.setLayoutY(y1);
        unshowPasswordRegularButton.setVisible(false);

        double x2 = showPasswordConfirmButton.getLayoutX(), y2 = showPasswordConfirmButton.getLayoutY();
        unshowPasswordConfirmButton.setLayoutX(x2);
        unshowPasswordConfirmButton.setLayoutY(y2);
        unshowPasswordConfirmButton.setVisible(false);
    }

    private void setupPasswordTextFields(){
        double x1 = passwordRegularPasswordField.getLayoutX(), y1 = passwordRegularPasswordField.getLayoutY();
        passwordRegularRevealedTextField.setLayoutX(x1);
        passwordRegularRevealedTextField.setLayoutY(y1);
        passwordRegularRevealedTextField.setVisible(false);
        passwordRegularRevealedTextField.textProperty().bindBidirectional(passwordRegularPasswordField.textProperty());   // synchronizing the text content

        double x2 = passwordConfirmPasswordField.getLayoutX(), y2 = passwordConfirmPasswordField.getLayoutY();
        passwordConfirmRevealedTextField.setLayoutX(x2);
        passwordConfirmRevealedTextField.setLayoutY(y2);
        passwordConfirmRevealedTextField.setVisible(false);
        passwordConfirmRevealedTextField.textProperty().bindBidirectional(passwordConfirmPasswordField.textProperty());   // synchronizing the text content
    }


}
