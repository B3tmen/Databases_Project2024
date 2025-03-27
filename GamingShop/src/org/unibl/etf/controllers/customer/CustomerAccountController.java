package org.unibl.etf.controllers.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.unibl.etf.database.dao.implementations.AddressDAOImpl;
import org.unibl.etf.database.dao.implementations.CityDAOImpl;
import org.unibl.etf.database.dao.implementations.CountryDAOImpl;
import org.unibl.etf.model.address.Address;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.service.AddressService;
import org.unibl.etf.service.CityService;
import org.unibl.etf.service.CountryService;
import org.unibl.etf.util.ImagePaths;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerAccountController implements Initializable {
    private CountryService countryService;
    private CityService cityService;
    private AddressService addressService;

    private Customer customer;

    @FXML
    private Circle imageHolderCircle;
    @FXML
    private Label fullNameLabel;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField zipcodeTextField;
    @FXML
    private Button closeButton;


    public CustomerAccountController(Customer customer) {
        this.customer = customer;
        this.countryService = new CountryService(new CountryDAOImpl());
        this.cityService = new CityService(new CityDAOImpl());
        this.addressService = new AddressService(new AddressDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource(ImagePaths.USER_PROFILE_LOGO).toExternalForm());
        imageHolderCircle.setFill(new ImagePattern(img));
        loadCustomer();

        disableTextFieldEditing();
    }

    private void loadCustomer() {
        fullNameLabel.setText(customer.getFirstName() + " " + customer.getLastName());
        usernameTextField.setText(customer.getUsername());
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        emailTextField.setText(customer.getEmail());
        phoneNumberTextField.setText(customer.getPhoneNumber());

        int addressId = customer.getAdressId();
        int cityId = addressService.getAddress(addressId).getCityId();

        City city = cityService.getCity(cityId);
        int countryId = city.getCountryId();

        Country country = countryService.getCountry(countryId);

        countryTextField.setText(country.getName());
        cityTextField.setText(city.getName());
        zipcodeTextField.setText(city.getZipCode());
    }

    private void disableTextFieldEditing(){
        usernameTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        emailTextField.setEditable(false);
        phoneNumberTextField.setEditable(false);
        countryTextField.setEditable(false);
        cityTextField.setEditable(false);
        zipcodeTextField.setEditable(false);
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    @FXML
    public void closeAccountViewAction(ActionEvent actionEvent) {
        // We get the source of the ActionEvent (the button) and its parent node (the anchor pane of the AccountView), we delete that node from its parent
        // of the top-view (the anchorPane in the CustomerMainView holding the CustomerAccountView)

        Node button = (Node) actionEvent.getSource();
        Node accountAnchorPane = button.getParent();

        AnchorPane parent = (AnchorPane) accountAnchorPane.getParent();
        parent.getChildren().remove(accountAnchorPane);

    }
}
