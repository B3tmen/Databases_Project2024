package org.unibl.etf.controllers.customer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.unibl.etf.database.dao.implementations.*;
import org.unibl.etf.model.enums.OrderStatusEnum;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.*;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.service.*;
import org.unibl.etf.util.AlertShower;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShoppingCartBuyController implements Initializable {
    private static final int ORDER_SHIPPING_VALUE = 10;

    private Customer customer;
    private ShoppingCart shoppingCart;
    private CustomerMainShopController mainShopController;
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    private AddressService addressService;
    private CityService cityService;
    private CountryService countryService;
    private OrderService orderService;
    private OrderItemService orderItemService;
    private CartItemService cartItemService;


    @FXML
    private TableColumn<CartItem, ImageView> imageColumn;
    @FXML
    private TableColumn<CartItem, String> productNameColumn;
    @FXML
    private TableColumn<CartItem, BigDecimal> priceColumn;
    @FXML
    private TableColumn<CartItem, Integer> ammountColumn;
    @FXML
    private TableColumn<CartItem, BigDecimal> totalPriceColumn;
    @FXML
    private TableView<CartItem> cartProductsTableView;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Label preShippingTotalPriceLabel;
    @FXML
    private Label postShippingTotalPriceLabel;
    @FXML
    private Button removeFromCartButton;
    @FXML
    private Button orderButton;


    public ShoppingCartBuyController(ShoppingCart shoppingCart, Customer customer, CustomerMainShopController controller){
        this.shoppingCart = shoppingCart;
        this.customer = customer;
        this.mainShopController = controller;

        addressService = new AddressService(new AddressDAOImpl());
        cityService = new CityService(new CityDAOImpl());
        countryService = new CountryService(new CountryDAOImpl());
        orderService = new OrderService(new OrderDAOImpl());
        orderItemService = new OrderItemService(new OrderItemDAOImpl());
        cartItemService = new CartItemService(new CartItemDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupTableView();
        setupTotalPriceLabels();
        setupTextFields();
    }

    private void setupTableColumns() {
        imageColumn.setCellValueFactory(param -> {
            CartItem cartItem = param.getValue();
            return new SimpleObjectProperty<>(cartItem.getProduct().getImage());
        });
        productNameColumn.setCellValueFactory(param ->{
            CartItem cartItem = param.getValue();
            return new SimpleStringProperty(cartItem.getProduct().getName());
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        ammountColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
    }

    private void setupTableView() {
        cartItems.addAll(shoppingCart.getCartItems());
        cartProductsTableView.setItems(cartItems);
    }

    private void setupTotalPriceLabels() {
        BigDecimal preShippingTotalPrice = shoppingCart.getGrandTotal();
        BigDecimal postShippingTotalPrice = preShippingTotalPrice.add(new BigDecimal(10));

        preShippingTotalPriceLabel.setText(preShippingTotalPrice.toPlainString());
        postShippingTotalPriceLabel.setText(postShippingTotalPrice.toPlainString());
    }

    private void setupTextFields(){
        int addressId = customer.getAdressId();
        String addressName = addressService.getAddress(addressId).getAddress1();
        int cityId = addressService.getAddress(addressId).getCityId();
        String cityName = cityService.getCity(cityId).getName();
        String zipCode = cityService.getCity(cityId).getZipCode();
        int countryId = cityService.getCity(cityId).getCountryId();
        String countryName = countryService.getCountry(countryId).getName();

        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        addressTextField.setText(addressName);
        cityTextField.setText(cityName);
        zipCodeTextField.setText(zipCode);
        countryTextField.setText(countryName);
        phoneNumberTextField.setText(customer.getPhoneNumber());
        emailTextField.setText(customer.getEmail());
    }

//    private boolean checkFields() {
//        boolean isValid = false;
//
//        if (firstNameTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "First Name field wasn't inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (lastNameTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Last Name field wasn't inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (countryTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Country wasn't inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (addressTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Address field wasn't inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (cityTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "City field wasn't correctly inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (zipCodeTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "ZIP Code field wasn't correctly inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (phoneNumberTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Phone number field wasn't correctly inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//        if (emailTextField.getText().isEmpty()) {
//            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Email field wasn't correctly inputted! (empty)", "Try again.");
//            alertShower.showAlert();
//        }
//
//        return isValid;
//    }

    @FXML
    public void makeOrderAction(ActionEvent actionEvent) {
        AlertShower alertShower = new AlertShower(Alert.AlertType.INFORMATION, "Order confirmation", "You are about to make an order.", "Are you sure you want to continue?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = alertShower.showAlert();
        if(answer.get() == ButtonType.YES){
            BigDecimal shipping = BigDecimal.valueOf(ORDER_SHIPPING_VALUE);
            Date currentDate = Date.valueOf(LocalDate.now());
            BigDecimal orderGrandTotal = shoppingCart.getGrandTotal();
            OrderStatus orderStatus = new OrderStatus(1, OrderStatusEnum.PENDING);
            int customerId = customer.getId();

            Order order = new Order(0, shipping, orderGrandTotal, currentDate, orderStatus, customerId);
            int orderId = orderService.addOrder(order);
            if(orderId > 0){
                AlertShower alertShower1 = new AlertShower(Alert.AlertType.CONFIRMATION, "Order success", "Your order was successfully placed!", "");
                alertShower1.showAlert();
            }

            for(CartItem cartItem : shoppingCart.getCartItems()){
                OrderItem orderItem = new OrderItem(orderId, cartItem.getProduct(), cartItem.getQuantity());

                int placedItems = orderItemService.addOrderItem(orderItem);
                order.addOrderItem(orderItem);
            }
        }
    }


    @FXML
    public void removeFromCartAction(ActionEvent actionEvent) {
        CartItem cartItem = (CartItem) cartProductsTableView.getSelectionModel().getSelectedItem();
        int numberOfDeletions = 0;
        if(cartItem != null){
            AlertShower questionShower = new AlertShower(Alert.AlertType.INFORMATION, "Removal of product from cart", "You are about to remove a Product in Your cart", "Are you sure you want to remove the selected product?", ButtonType.YES, ButtonType.NO);
            var answer = questionShower.showAlert();
            if(answer.get().equals(ButtonType.YES)){
                numberOfDeletions = cartItemService.deleteCartItem(cartItem);

                if(numberOfDeletions > 0){
                    AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Selected Product succesfully deleted.", "");
                    alertShower.showAlert();

                    shoppingCart.removeFromCart(cartItem);
                    cartItems.remove(cartItem);
                    mainShopController.setupLabels();
                    updateFieldsFromRemoval();

                    cartProductsTableView.getSelectionModel().select(null);
                    cartProductsTableView.refresh();
                }
            }
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Row for removal not selected!", "Try again.");
            alertShower.showAlert();
        }
    }

    private void updateFieldsFromRemoval(){
        BigDecimal preShippingTotalPrice = shoppingCart.getGrandTotal();
        BigDecimal postShippingTotalPrice = preShippingTotalPrice.add(new BigDecimal(10));

        preShippingTotalPriceLabel.setText(preShippingTotalPrice.toPlainString());
        postShippingTotalPriceLabel.setText(postShippingTotalPrice.toPlainString());
    }
}
