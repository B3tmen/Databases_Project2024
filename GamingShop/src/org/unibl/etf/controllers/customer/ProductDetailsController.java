package org.unibl.etf.controllers.customer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import org.unibl.etf.database.dao.implementations.CustomerDAOImpl;
import org.unibl.etf.database.dao.implementations.DiscountDAOImpl;
import org.unibl.etf.database.dao.implementations.ReviewDAOImpl;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.product.Review;
import org.unibl.etf.model.purchases.Discount;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.service.CustomerService;
import org.unibl.etf.service.DiscountService;
import org.unibl.etf.service.ReviewService;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class ProductDetailsController implements Initializable {
    private CustomerService customerService;
    private DiscountService discountService;
    private ReviewService reviewService;

    private int customerId;
    private Product product;
    private CustomerMainShopController customerMainShopController;

    private ObservableList<Review> reviews;


    @FXML
    private ImageView productImageView;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label shortDescriptionLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label currencyLabel;
    @FXML
    private TextField availableProductsTextField;
    @FXML
    private Button addToCartButton;
    @FXML
    private Label categoryListLabel;
    @FXML
    private Label productNameTabPaneLabel;
    @FXML
    private TabPane productDetailsTabPane;
    @FXML
    private TextArea productDescriptionTextArea;
    @FXML
    private Label discountPercentageLabel;
    @FXML
    private Label discountedPriceLabel;
    @FXML
    private Label currencyLabel1;
    @FXML
    private Line discountStrikeLine;
    @FXML
    private TableView<Review> reviewTableView;
    @FXML
    private TableColumn<Review, String> reviewTitleColumn;
    @FXML
    private TableColumn<Review, String> reviewScoreReview;
    @FXML
    private TableColumn<Review, String> reviewCommentColumn;
    @FXML
    private TableColumn<Review, String> customerReviewerColumn;
    @FXML
    private Button reviewButton;
    @FXML
    private Button addNumberOfProductsButton;
    @FXML
    private Button subtractNumberOfProductsButton;
    @FXML
    private Label productUnavailableLabel;

    public ProductDetailsController() {
    }

    public ProductDetailsController(Product product, CustomerMainShopController controller) {
        this.customerId = controller.getCustomerId();
        this.product = product;
        this.customerService = new CustomerService(new CustomerDAOImpl());
        this.discountService = new DiscountService(new DiscountDAOImpl());
        this.reviewService = new ReviewService(new ReviewDAOImpl());
        this.customerMainShopController = controller;
        this.reviews = FXCollections.observableArrayList(reviewService.getAll(product.getId()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTextFields();
        setupTableView();
    }

    private void setupTableView(){
        Customer customer = customerService.getCustomerById(customerId);
        customerReviewerColumn.setCellValueFactory(param -> new SimpleStringProperty(customer.getUsername()));
        reviewTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        reviewScoreReview.setCellValueFactory(new PropertyValueFactory<>("score"));
        reviewCommentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        reviewTableView.setItems(reviews);
    }

    public void refreshReviewTableView() {
        reviews.setAll(reviewService.getAll(product.getId()));
        reviewTableView.refresh();
    }

    private void setupTextFields() {
        this.productImageView.setImage(product.getImage().getImage());
        this.productNameLabel.setText(product.getName());
        this.shortDescriptionLabel.setText(product.getDescription());
        this.priceLabel.setText(String.valueOf(product.getPrice()));
        this.currencyLabel.setText("BAM");
        this.categoryListLabel.setText(product.getCategoryName());

        this.productNameTabPaneLabel.setText(product.getName());

        setupProductDescriptionArea();
        setupDiscount();
        setupAvailableProducts();
    }

    private void setupProductDescriptionArea() {
        String text = "Description: " + product.getDescription();
        text += "\n" + "Manufacturer: " + product.getManufacturerName();
        text += "\n" + "Available units: " + product.getQuantityAvailable();
        text += "\n" + "Warranty (months): " + product.getWarrantyMonths();

        this.productDescriptionTextArea.setText(text);
    }

    private void setupDiscount(){
        if(product.getDiscountId() != 0){
            Discount discount = discountService.getDiscountById(product.getDiscountId());
            Date todayDate = Date.valueOf(LocalDate.now());

            if(validDiscountDates(discount, todayDate)){
                discountPercentageLabel.setText("-" + discount.getPercentage() + " " + "%");
                BigDecimal discountedPrice = product.getDiscountedPrice();
                discountedPriceLabel.setText(String.valueOf(discountedPrice));
            }
            else{
                setDiscountFieldsNotVisible();
            }
        }
        else{
            setDiscountFieldsNotVisible();
        }
    }

    private void setupAvailableProducts() {
        if(product.getQuantityAvailable() > 0){
            this.availableProductsTextField.setText("1");
            this.productUnavailableLabel.setVisible(false);
        }
        else{
            this.availableProductsTextField.setText("0");
            this.addToCartButton.setDisable(true);
            this.reviewButton.setDisable(true);
            this.subtractNumberOfProductsButton.setDisable(true);
            this.addNumberOfProductsButton.setDisable(true);
            this.productUnavailableLabel.setVisible(true);
        }
    }

    private boolean validDiscountDates(Discount discount, Date todayDate){
        return todayDate.compareTo(discount.getDateFrom()) >= 0 && todayDate.compareTo(discount.getDateTo()) <= 0;
    }

    private void setDiscountFieldsNotVisible(){
        discountStrikeLine.setVisible(false);
        discountPercentageLabel.setVisible(false);
        discountedPriceLabel.setVisible(false);
        currencyLabel1.setVisible(false);
    }


    @FXML
    public void addNumberOfProductsAction(ActionEvent actionEvent) {
        int maxQuantity = product.getQuantityAvailable();
        int currentQuantity = Integer.parseInt(availableProductsTextField.getText());

        if(currentQuantity < maxQuantity) {
            currentQuantity++;
            availableProductsTextField.setText(String.valueOf(currentQuantity));
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Max quantity of product already reached.", "No more products available.");
            alertShower.showAlert();
        }

    }

    @FXML
    public void subtractNumberOfProductsAction(ActionEvent actionEvent) {
        int maxQuantity = product.getQuantityAvailable();
        int currentQuantity = Integer.parseInt(availableProductsTextField.getText());

        if(currentQuantity > 1) {
            currentQuantity--;
            availableProductsTextField.setText(String.valueOf(currentQuantity));
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Select at least one product for buying.", "");
            alertShower.showAlert();
        }
    }

    @FXML
    public void leaveReviewAction(ActionEvent actionEvent) {
        FxmlViewManager viewManager = new FxmlViewManager(FxmlPaths.CUSTOMER_ADD_PRODUCT_REVIEW_FXML, "Product review", new ProductReviewController(product.getId(), customerId, this));
        viewManager.showView();
    }

    @FXML
    public void addToCartAction(ActionEvent actionEvent) {
        int quantity = Integer.parseInt(availableProductsTextField.getText());
        customerMainShopController.addToCart(product, quantity);
    }
}
